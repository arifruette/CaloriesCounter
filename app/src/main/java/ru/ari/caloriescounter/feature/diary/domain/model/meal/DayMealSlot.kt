package ru.ari.caloriescounter.feature.diary.domain.model.meal

data class DayMealSlot(
    val mealKey: String,
    val title: String,
    val sortOrder: Int,
    val isBase: Boolean,
)

