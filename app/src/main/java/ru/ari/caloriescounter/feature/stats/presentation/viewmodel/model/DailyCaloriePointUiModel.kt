package ru.ari.caloriescounter.feature.stats.presentation.viewmodel.model

data class DailyCaloriePointUiModel(
    val dayLabel: String,
    val calories: Int,
    val isGoalCompleted: Boolean,
)
