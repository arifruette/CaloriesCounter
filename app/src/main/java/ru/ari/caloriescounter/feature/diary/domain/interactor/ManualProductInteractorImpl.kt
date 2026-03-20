package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.ManualProductRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.product.ManualProduct

class ManualProductInteractorImpl @Inject constructor(
    private val repository: ManualProductRepository,
) : ManualProductInteractor {
    override fun observeAll(): Flow<List<ManualProduct>> = repository.observeAll()

    override suspend fun add(nameRu: String, nutritionPer100g: NutritionPer100g): Long =
        repository.add(nameRu = nameRu, nutritionPer100g = nutritionPer100g)

    override suspend fun delete(productId: Long) {
        repository.delete(productId)
    }
}
