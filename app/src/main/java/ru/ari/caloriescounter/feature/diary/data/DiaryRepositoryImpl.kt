package ru.ari.caloriescounter.feature.diary.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.DayMealSlotDao
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity
import ru.ari.caloriescounter.core.database.entity.DayMealSlotEntity
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DeletedMealPayload
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.diary.MealSummary
import ru.ari.caloriescounter.feature.diary.domain.model.meal.DayMealSlot
import ru.ari.caloriescounter.feature.diary.domain.model.meal.defaultMealSlots
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val calorieEntryDao: CalorieEntryDao,
    private val dayMealSlotDao: DayMealSlotDao,
) : DiaryRepository {

    override fun observeDayDiary(date: LocalDate): Flow<DayDiary> {
        val dayDate = date.toString()
        val entriesFlow = calorieEntryDao.observeEntriesByDate(dayDate)
        val slotsFlow = dayMealSlotDao.observeSlotsByDate(dayDate)
            .onStart { ensureDefaultMealSlots(dayDate) }

        return combine(entriesFlow, slotsFlow) { entries, slots ->
            val dayEntries = entries.map { it.toDomain() }
            val summaries = slots
                .sortedBy { it.sortOrder }
                .map { slot ->
                    val mealEntries = dayEntries.filter { it.mealKey == slot.mealKey }
                    MealSummary(
                        mealKey = slot.mealKey,
                        title = slot.title,
                        entriesCount = mealEntries.size,
                        totalCalories = mealEntries.sumOf { it.totalCalories() },
                        totalProtein = mealEntries.sumOf { it.totalProtein() },
                        totalFat = mealEntries.sumOf { it.totalFat() },
                        totalCarbs = mealEntries.sumOf { it.totalCarbs() },
                    )
                }

            DayDiary(
                date = date,
                entries = dayEntries,
                mealSummaries = summaries,
                totalCalories = dayEntries.sumOf { it.totalCalories() },
                totalProtein = dayEntries.sumOf { it.totalProtein() },
                totalFat = dayEntries.sumOf { it.totalFat() },
                totalCarbs = dayEntries.sumOf { it.totalCarbs() },
            )
        }
    }

    override fun observeMealSlots(date: LocalDate): Flow<List<DayMealSlot>> {
        val dayDate = date.toString()
        return dayMealSlotDao.observeSlotsByDate(dayDate)
            .onStart { ensureDefaultMealSlots(dayDate) }
            .map { slots -> slots.map { it.toDomain() } }
    }

    override fun observeMealEntries(date: LocalDate, mealKey: String): Flow<List<DiaryEntry>> =
        calorieEntryDao.observeEntriesByDateAndMeal(date.toString(), mealKey)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun addCustomMealSlot(date: LocalDate, title: String) {
        val dayDate = date.toString()
        ensureDefaultMealSlots(dayDate)
        val nextSortOrder = dayMealSlotDao.maxSortOrder(dayDate) + 1
        val normalizedTitle = title.trim()
        if (normalizedTitle.isBlank()) return
        dayMealSlotDao.insertSlot(
            DayMealSlotEntity(
                dayDate = dayDate,
                mealKey = generateCustomMealKey(),
                title = normalizedTitle,
                sortOrder = nextSortOrder,
                isBase = false,
            ),
        )
    }

    override suspend fun renameMealSlot(date: LocalDate, mealKey: String, title: String) {
        val normalizedTitle = title.trim()
        if (normalizedTitle.isBlank()) return
        dayMealSlotDao.updateSlotTitle(dayDate = date.toString(), mealKey = mealKey, title = normalizedTitle)
    }

    override suspend fun deleteMealSlotWithEntries(date: LocalDate, mealKey: String): DeletedMealPayload? {
        val dayDate = date.toString()
        val slot = dayMealSlotDao.getSlotByDateAndKey(dayDate, mealKey) ?: return null
        val deletedEntries = calorieEntryDao.getEntriesByDateAndMeal(dayDate, mealKey).map { it.toDomain() }
        dayMealSlotDao.deleteSlot(dayDate, mealKey)
        calorieEntryDao.deleteByDateAndMeal(dayDate, mealKey)
        return DeletedMealPayload(
            date = date,
            slot = slot.toDomain(),
            deletedEntries = deletedEntries,
        )
    }

    override suspend fun restoreDeletedMeal(payload: DeletedMealPayload) {
        dayMealSlotDao.insertSlot(payload.slot.toEntity(payload.date))
        calorieEntryDao.insertEntries(payload.deletedEntries.map { it.toEntity() })
    }

    override suspend fun addEntry(entry: DiaryEntry) {
        calorieEntryDao.insertEntry(entry.toEntity())
    }

    override suspend fun updateEntryPortion(entryId: Long, grams: Double) {
        calorieEntryDao.updatePortionById(entryId, grams)
    }

    override suspend fun removeEntry(entryId: Long) {
        calorieEntryDao.deleteById(entryId)
    }

    private suspend fun ensureDefaultMealSlots(dayDate: String) {
        val existing = dayMealSlotDao.getSlotsByDate(dayDate)
        if (existing.isNotEmpty()) return

        val baseSlots = defaultMealSlots.map { slot ->
            DayMealSlotEntity(
                dayDate = dayDate,
                mealKey = slot.mealKey,
                title = slot.title,
                sortOrder = slot.sortOrder,
                isBase = true,
            )
        }
        val additionalFromEntries = calorieEntryDao.getEntriesByDate(dayDate)
            .map { it.mealType }
            .distinct()
            .filterNot { key -> baseSlots.any { it.mealKey == key } }
            .mapIndexed { index, mealKey ->
                DayMealSlotEntity(
                    dayDate = dayDate,
                    mealKey = mealKey,
                    title = mealKey.toDisplayMealTitle(),
                    sortOrder = baseSlots.size + index,
                    isBase = false,
                )
            }

        dayMealSlotDao.insertSlots(baseSlots + additionalFromEntries)
    }

    private fun CalorieEntryEntity.toDomain(): DiaryEntry {
        val resolvedSource = runCatching { ProductSource.valueOf(productSource) }
            .getOrElse { ProductSource.OPEN_FOOD_FACTS }

        return DiaryEntry(
            id = id,
            date = LocalDate.parse(dayDate),
            mealKey = mealType,
            product = ProductRef(
                source = resolvedSource,
                externalId = productExternalId,
                barcode = productBarcode,
                nameRu = productNameRu,
                nameOriginal = productNameOriginal,
            ),
            nutritionPer100g = NutritionPer100g(
                calories = caloriesPer100g,
                protein = proteinPer100g,
                fat = fatPer100g,
                carbs = carbsPer100g,
            ),
            portion = Portion(grams = portionGrams),
        )
    }

    private fun DiaryEntry.toEntity(): CalorieEntryEntity =
        CalorieEntryEntity(
            id = 0,
            dayDate = date.toString(),
            mealType = mealKey,
            productSource = product.source.name,
            productExternalId = product.externalId,
            productBarcode = product.barcode,
            productNameRu = product.nameRu,
            productNameOriginal = product.nameOriginal,
            caloriesPer100g = nutritionPer100g.calories,
            proteinPer100g = nutritionPer100g.protein,
            fatPer100g = nutritionPer100g.fat,
            carbsPer100g = nutritionPer100g.carbs,
            portionGrams = portion.grams,
        )

    private fun DayMealSlotEntity.toDomain(): DayMealSlot =
        DayMealSlot(
            mealKey = mealKey,
            title = title,
            sortOrder = sortOrder,
            isBase = isBase,
        )

    private fun DayMealSlot.toEntity(date: LocalDate): DayMealSlotEntity =
        DayMealSlotEntity(
            dayDate = date.toString(),
            mealKey = mealKey,
            title = title,
            sortOrder = sortOrder,
            isBase = isBase,
        )

    private fun generateCustomMealKey(): String = "custom_${System.currentTimeMillis()}"
}

private const val CUSTOM_MEAL_FALLBACK_TITLE = "Прием пищи"
private fun String.toDisplayMealTitle(): String = when (lowercase()) {
    "breakfast" -> "Завтрак"
    "lunch" -> "Обед"
    "dinner" -> "Ужин"
    "snack" -> "Перекус"
    else -> replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
