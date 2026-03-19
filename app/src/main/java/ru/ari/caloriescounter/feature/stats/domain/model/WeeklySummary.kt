package ru.ari.caloriescounter.feature.stats.domain.model

import java.time.LocalDate

data class WeeklySummary(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dailyCalories: List<DailyCaloriePoint>,
    val averageCaloriesPerDay: Double,
    val goalCompletedDays: Int,
    val averageProteinPerDay: Double,
    val averageMealsPerDay: Double,
    val bestStreakDays: Int,
    val currentStreakDays: Int,
    val loggedDaysInWindow: Int,
)
