package ru.ari.caloriescounter.feature.stats.domain.model

import java.time.LocalDate

data class DailyCaloriePoint(
    val date: LocalDate,
    val calories: Double,
    val isGoalCompleted: Boolean,
)
