package ru.ari.caloriescounter.feature.diary.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.diary.MealSummary
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

@Singleton
class DiaryRepositoryImpl @Inject constructor() : DiaryRepository {

    private val entries = MutableStateFlow<List<DiaryEntry>>(emptyList())

    override fun observeDayDiary(date: LocalDate): Flow<DayDiary> =
        entries.map { current ->
            val dayEntries = current.filter { it.date == date }
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
        entries.map { current ->
            current.filter { it.date == date && it.mealType == mealType }
        }

    override suspend fun addEntry(entry: DiaryEntry) {
        entries.value += entry
    }

    override suspend fun removeEntry(entryId: Long) {
        entries.value = entries.value.filterNot { it.id == entryId }
    }
}
