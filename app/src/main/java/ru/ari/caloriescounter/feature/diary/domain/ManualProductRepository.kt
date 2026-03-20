package ru.ari.caloriescounter.feature.diary.domain

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.product.ManualProduct

interface ManualProductRepository {
    fun observeAll(): Flow<List<ManualProduct>>
    suspend fun add(nameRu: String, nutritionPer100g: NutritionPer100g): Long
    suspend fun delete(productId: Long)
}
