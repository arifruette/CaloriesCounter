package ru.ari.caloriescounter.feature.diary.domain.model.product

import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g

data class ProductCandidate(
    val source: ProductSource,
    val externalId: String?,
    val barcode: String?,
    val displayNameRu: String,
    val nutritionPer100g: NutritionPer100g,
)
