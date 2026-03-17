package ru.ari.caloriescounter.feature.diary.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.diary.MealSummary
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val calorieEntryDao: CalorieEntryDao,
) : DiaryRepository {

    override fun observeDayDiary(date: LocalDate): Flow<DayDiary> =
        calorieEntryDao.observeEntriesByDate(date.toString()).map { entities ->
            val dayEntries = entities.map { it.toDomain() }
            val summaries = MealType.entries.associateWith { mealType ->
                val mealEntries = dayEntries.filter { it.mealType == mealType }
                MealSummary(
                    mealType = mealType,
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
                totalCalories = summaries.values.sumOf { it.totalCalories },
                totalProtein = summaries.values.sumOf { it.totalProtein },
                totalFat = summaries.values.sumOf { it.totalFat },
                totalCarbs = summaries.values.sumOf { it.totalCarbs },
            )
        }

    override fun observeMealEntries(date: LocalDate, mealType: MealType): Flow<List<DiaryEntry>> =
        calorieEntryDao.observeEntriesByDateAndMeal(date.toString(), mealType.name)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun addEntry(entry: DiaryEntry) {
        calorieEntryDao.insertEntry(entry.toEntity())
    }

    override suspend fun removeEntry(entryId: Long) {
        calorieEntryDao.deleteById(entryId)
    }

    private fun CalorieEntryEntity.toDomain(): DiaryEntry {
        val resolvedMealType = runCatching { MealType.valueOf(mealType) }.getOrElse { MealType.SNACK }
        val resolvedSource = runCatching { ProductSource.valueOf(productSource) }
            .getOrElse { ProductSource.OPEN_FOOD_FACTS }

        return DiaryEntry(
            id = id,
            date = LocalDate.parse(dayDate),
            mealType = resolvedMealType,
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
            mealType = mealType.name,
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
}
