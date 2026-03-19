package ru.ari.caloriescounter.feature.stats.presentation.viewmodel.model

data class WeeklySummaryUiModel(
    val dailyCalories: List<DailyCaloriePointUiModel>,
    val averageCaloriesPerDay: Int,
    val goalCompletedDays: Int,
    val averageProteinPerDay: Double,
    val bestStreakDays: Int,
    val currentStreakDays: Int,
    val loggedDaysInWindow: Int,
)
