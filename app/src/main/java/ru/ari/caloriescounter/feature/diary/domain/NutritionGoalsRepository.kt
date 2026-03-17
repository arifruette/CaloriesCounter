package ru.ari.caloriescounter.feature.diary.domain

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals

interface NutritionGoalsRepository {
    fun observeGoals(): Flow<NutritionGoals>
    suspend fun updateGoals(goals: NutritionGoals)
}
