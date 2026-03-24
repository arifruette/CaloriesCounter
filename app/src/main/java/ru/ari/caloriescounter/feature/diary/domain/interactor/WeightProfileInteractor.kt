package ru.ari.caloriescounter.feature.diary.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.weight.WeightProfile

interface WeightProfileInteractor {
    fun observeWeightProfile(): Flow<WeightProfile>
    suspend fun initializeWeightProfile(
        initialWeightKg: Double,
        currentWeightKg: Double,
        targetWeightKg: Double,
    )
    suspend fun updateInitialWeight(weightKg: Double)
    suspend fun updateCurrentWeight(weightKg: Double)
    suspend fun updateTargetWeight(weightKg: Double)
}
