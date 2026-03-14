package ru.ari.caloriescounter.feature.diary.domain

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.DayDiary

interface DiaryInteractor {
    fun observeDiary(date: LocalDate): Flow<DayDiary>
}

class DiaryInteractorImpl @Inject constructor(
    private val repository: DiaryRepository,
) : DiaryInteractor {
    override fun observeDiary(date: LocalDate): Flow<DayDiary> = repository.observeDayDiary(date)
}

