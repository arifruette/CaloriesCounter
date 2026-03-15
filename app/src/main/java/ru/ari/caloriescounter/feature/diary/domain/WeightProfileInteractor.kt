package ru.ari.caloriescounter.feature.diary.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.WeightProfile

interface WeightProfileInteractor {
    fun observeWeightProfile(): Flow<WeightProfile>
    suspend fun updateCurrentWeight(weightKg: Double)
    suspend fun updateTargetWeight(weightKg: Double)
}

class WeightProfileInteractorImpl @Inject constructor(
    private val repository: WeightProfileRepository,
) : WeightProfileInteractor {

    override fun observeWeightProfile(): Flow<WeightProfile> = repository.observeWeightProfile()

    override suspend fun updateCurrentWeight(weightKg: Double) {
        repository.updateCurrentWeight(weightKg)
    }

    override suspend fun updateTargetWeight(weightKg: Double) {
        repository.updateTargetWeight(weightKg)
    }
}
