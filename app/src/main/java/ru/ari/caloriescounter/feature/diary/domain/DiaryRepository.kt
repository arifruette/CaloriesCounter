package ru.ari.caloriescounter.feature.diary.domain

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

interface DiaryRepository {
    fun observeDayDiary(date: LocalDate): Flow<DayDiary>
    fun observeMealEntries(date: LocalDate, mealType: MealType): Flow<List<DiaryEntry>>
    suspend fun addEntry(entry: DiaryEntry)
    suspend fun removeEntry(entryId: Long)
}

