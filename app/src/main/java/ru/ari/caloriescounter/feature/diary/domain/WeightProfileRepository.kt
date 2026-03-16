package ru.ari.caloriescounter.feature.diary.domain

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.weight.WeightProfile

interface WeightProfileRepository {
    fun observeWeightProfile(): Flow<WeightProfile>
    suspend fun updateCurrentWeight(weightKg: Double)
    suspend fun updateTargetWeight(weightKg: Double)
}
