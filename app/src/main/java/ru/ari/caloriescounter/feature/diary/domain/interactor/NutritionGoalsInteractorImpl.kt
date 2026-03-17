package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.NutritionGoalsRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals

class NutritionGoalsInteractorImpl @Inject constructor(
    private val repository: NutritionGoalsRepository,
) : NutritionGoalsInteractor {
    override fun observeGoals(): Flow<NutritionGoals> = repository.observeGoals()

    override suspend fun updateGoals(goals: NutritionGoals) {
        repository.updateGoals(goals)
    }
}
