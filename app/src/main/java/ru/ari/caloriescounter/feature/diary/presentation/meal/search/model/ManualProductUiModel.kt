package ru.ari.caloriescounter.feature.diary.presentation.meal.search.model

data class ManualProductUiModel(
    val id: Long,
    val nameRu: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
)
