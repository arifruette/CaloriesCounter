package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.model.weight.WeightProfile

class WeightProfileInteractorImpl @Inject constructor(
    private val repository: WeightProfileRepository,
) : WeightProfileInteractor {

    override fun observeWeightProfile(): Flow<WeightProfile> = repository.observeWeightProfile()

    override suspend fun initializeWeightProfile(
        initialWeightKg: Double,
        currentWeightKg: Double,
        targetWeightKg: Double,
    ) {
        repository.initializeWeightProfile(initialWeightKg, currentWeightKg, targetWeightKg)
    }

    override suspend fun updateInitialWeight(weightKg: Double) {
        repository.updateInitialWeight(weightKg)
    }

    override suspend fun updateCurrentWeight(weightKg: Double) {
        repository.updateCurrentWeight(weightKg)
    }

    override suspend fun updateTargetWeight(weightKg: Double) {
        repository.updateTargetWeight(weightKg)
    }
}
