package ru.ari.caloriescounter.feature.diary.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionRecommendation
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile

interface NutritionGoalsInteractor {
    fun observeGoals(): Flow<NutritionGoals>
    fun calculateRecommendation(profile: UserProfile, currentWeightKg: Double): NutritionRecommendation
    suspend fun updateGoals(goals: NutritionGoals)
}
