package ru.ari.caloriescounter.feature.diary.data

import javax.inject.Inject
import ru.ari.caloriescounter.feature.diary.data.remote.api.OpenFoodFactsApi
import ru.ari.caloriescounter.feature.diary.data.remote.dto.OpenFoodFactsProductDto
import ru.ari.caloriescounter.feature.diary.domain.ProductSearchRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductCandidate
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource

class ProductSearchRepositoryImpl @Inject constructor(
    private val api: OpenFoodFactsApi,
) : ProductSearchRepository {

    override suspend fun searchByName(query: String): List<ProductCandidate> =
        runCatching {
            api.searchByName(query).products.mapNotNull(::mapToCandidate)
        }.getOrDefault(emptyList())

    override suspend fun searchByBarcode(barcode: String): ProductCandidate? =
        runCatching {
            api.productByBarcode(barcode).product?.let(::mapToCandidate)
        }.getOrNull()

    private fun mapToCandidate(product: OpenFoodFactsProductDto): ProductCandidate? {
        val name = product.productNameRu ?: product.productName ?: return null
        val nutriments = product.nutriments ?: return null
        return ProductCandidate(
            source = ProductSource.OPEN_FOOD_FACTS,
            externalId = product.code,
            barcode = product.code,
            displayNameRu = name,
            nutritionPer100g = NutritionPer100g(
                calories = nutriments.energyKcal100g ?: 0.0,
                protein = nutriments.proteins100g ?: 0.0,
                fat = nutriments.fat100g ?: 0.0,
                carbs = nutriments.carbohydrates100g ?: 0.0,
            ),
        )
    }
}

