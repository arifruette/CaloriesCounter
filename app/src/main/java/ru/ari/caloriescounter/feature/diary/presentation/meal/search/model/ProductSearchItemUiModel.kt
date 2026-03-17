package ru.ari.caloriescounter.feature.diary.presentation.meal.search.model

import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource

data class ProductSearchItemUiModel(
    val source: ProductSource,
    val externalId: String?,
    val barcode: String?,
    val nameRu: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
)
