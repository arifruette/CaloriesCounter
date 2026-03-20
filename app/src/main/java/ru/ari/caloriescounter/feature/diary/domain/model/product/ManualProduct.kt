package ru.ari.caloriescounter.feature.diary.domain.model.product

import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g

data class ManualProduct(
    val id: Long,
    val nameRu: String,
    val nutritionPer100g: NutritionPer100g,
)
