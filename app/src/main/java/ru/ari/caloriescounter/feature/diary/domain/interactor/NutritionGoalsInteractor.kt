package ru.ari.caloriescounter.feature.diary.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals

interface NutritionGoalsInteractor {
    fun observeGoals(): Flow<NutritionGoals>
    suspend fun updateGoals(goals: NutritionGoals)
}
