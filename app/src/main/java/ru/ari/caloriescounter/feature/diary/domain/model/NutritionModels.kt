package ru.ari.caloriescounter.feature.diary.domain.model

data class NutritionPer100g(
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
)

data class Portion(
    val grams: Double,
)

enum class ProductSource {
    OPEN_FOOD_FACTS,
    MANUAL,
}

data class ProductRef(
    val source: ProductSource,
    val externalId: String?,
    val barcode: String?,
    val nameRu: String,
    val nameOriginal: String?,
)

data class ProductCandidate(
    val source: ProductSource,
    val externalId: String?,
    val barcode: String?,
    val displayNameRu: String,
    val nutritionPer100g: NutritionPer100g,
)
