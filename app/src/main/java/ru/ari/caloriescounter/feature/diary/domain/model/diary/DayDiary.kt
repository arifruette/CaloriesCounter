package ru.ari.caloriescounter.feature.diary.domain.model.diary

import java.time.LocalDate

data class DayDiary(
    val date: LocalDate,
    val entries: List<DiaryEntry>,
    val mealSummaries: List<MealSummary>,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)
