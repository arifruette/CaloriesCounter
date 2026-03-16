package ru.ari.caloriescounter.feature.diary.domain.model.diary

import java.time.LocalDate
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

data class DayDiary(
    val date: LocalDate,
    val entries: List<DiaryEntry>,
    val mealSummaries: Map<MealType, MealSummary>,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)
