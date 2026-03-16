package ru.ari.caloriescounter.feature.diary.domain.interactor

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary

interface DiaryInteractor {
    fun observeDiary(date: LocalDate): Flow<DayDiary>
}
