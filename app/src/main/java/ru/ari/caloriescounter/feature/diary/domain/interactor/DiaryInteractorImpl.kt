package ru.ari.caloriescounter.feature.diary.domain.interactor

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

class DiaryInteractorImpl @Inject constructor(
    private val repository: DiaryRepository,
) : DiaryInteractor {
    override fun observeDiary(date: LocalDate): Flow<DayDiary> = repository.observeDayDiary(date)

    override fun observeMealEntries(date: LocalDate, mealType: MealType): Flow<List<DiaryEntry>> =
        repository.observeMealEntries(date, mealType)

    override suspend fun addEntry(entry: DiaryEntry) {
        repository.addEntry(entry)
    }

    override suspend fun removeEntry(entryId: Long) {
        repository.removeEntry(entryId)
    }
}
