package ru.ari.caloriescounter.feature.diary.domain.interactor

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DeletedMealPayload
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.DayMealSlot

class DiaryInteractorImpl @Inject constructor(
    private val repository: DiaryRepository,
) : DiaryInteractor {
    override fun observeDiary(date: LocalDate): Flow<DayDiary> = repository.observeDayDiary(date)

    override fun observeMealSlots(date: LocalDate): Flow<List<DayMealSlot>> =
        repository.observeMealSlots(date)

    override fun observeMealEntries(date: LocalDate, mealKey: String): Flow<List<DiaryEntry>> =
        repository.observeMealEntries(date, mealKey)

    override suspend fun addCustomMealSlot(date: LocalDate, title: String) {
        repository.addCustomMealSlot(date, title)
    }

    override suspend fun renameMealSlot(date: LocalDate, mealKey: String, title: String) {
        repository.renameMealSlot(date, mealKey, title)
    }

    override suspend fun deleteMealSlotWithEntries(date: LocalDate, mealKey: String): DeletedMealPayload? =
        repository.deleteMealSlotWithEntries(date, mealKey)

    override suspend fun restoreDeletedMeal(payload: DeletedMealPayload) {
        repository.restoreDeletedMeal(payload)
    }

    override suspend fun addEntry(entry: DiaryEntry) {
        repository.addEntry(entry)
    }

    override suspend fun updateEntryPortion(entryId: Long, grams: Double) {
        repository.updateEntryPortion(entryId, grams)
    }

    override suspend fun removeEntry(entryId: Long) {
        repository.removeEntry(entryId)
    }
}
