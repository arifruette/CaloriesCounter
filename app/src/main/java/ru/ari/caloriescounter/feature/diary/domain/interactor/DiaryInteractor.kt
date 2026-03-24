package ru.ari.caloriescounter.feature.diary.domain.interactor

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DeletedMealPayload
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.DayMealSlot

interface DiaryInteractor {
    fun observeDiary(date: LocalDate): Flow<DayDiary>
    fun observeMealSlots(date: LocalDate): Flow<List<DayMealSlot>>
    fun observeMealEntries(date: LocalDate, mealKey: String): Flow<List<DiaryEntry>>
    suspend fun addCustomMealSlot(date: LocalDate, title: String)
    suspend fun renameMealSlot(date: LocalDate, mealKey: String, title: String)
    suspend fun deleteMealSlotWithEntries(date: LocalDate, mealKey: String): DeletedMealPayload?
    suspend fun restoreDeletedMeal(payload: DeletedMealPayload)
    suspend fun addEntry(entry: DiaryEntry)
    suspend fun updateEntryPortion(entryId: Long, grams: Double)
    suspend fun removeEntry(entryId: Long)
}
