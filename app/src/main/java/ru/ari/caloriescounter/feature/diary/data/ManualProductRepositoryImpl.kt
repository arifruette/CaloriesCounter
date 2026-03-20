package ru.ari.caloriescounter.feature.diary.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.database.dao.ManualProductDao
import ru.ari.caloriescounter.core.database.entity.ManualProductEntity
import ru.ari.caloriescounter.feature.diary.domain.ManualProductRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.product.ManualProduct

class ManualProductRepositoryImpl @Inject constructor(
    private val manualProductDao: ManualProductDao,
) : ManualProductRepository {

    override fun observeAll(): Flow<List<ManualProduct>> =
        manualProductDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun add(nameRu: String, nutritionPer100g: NutritionPer100g): Long =
        manualProductDao.insert(
            ManualProductEntity(
                nameRu = nameRu,
                caloriesPer100g = nutritionPer100g.calories,
                proteinPer100g = nutritionPer100g.protein,
                fatPer100g = nutritionPer100g.fat,
                carbsPer100g = nutritionPer100g.carbs,
                createdAtEpochMillis = System.currentTimeMillis(),
            ),
        )

    override suspend fun delete(productId: Long) {
        manualProductDao.deleteById(productId)
    }
}

private fun ManualProductEntity.toDomain(): ManualProduct =
    ManualProduct(
        id = id,
        nameRu = nameRu,
        nutritionPer100g = NutritionPer100g(
            calories = caloriesPer100g,
            protein = proteinPer100g,
            fat = fatPer100g,
            carbs = carbsPer100g,
        ),
    )
