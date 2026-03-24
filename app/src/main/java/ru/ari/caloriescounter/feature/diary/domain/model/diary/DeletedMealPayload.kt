package ru.ari.caloriescounter.feature.diary.domain.model.diary

import java.time.LocalDate
import ru.ari.caloriescounter.feature.diary.domain.model.meal.DayMealSlot

data class DeletedMealPayload(
    val date: LocalDate,
    val slot: DayMealSlot,
    val deletedEntries: List<DiaryEntry>,
)

