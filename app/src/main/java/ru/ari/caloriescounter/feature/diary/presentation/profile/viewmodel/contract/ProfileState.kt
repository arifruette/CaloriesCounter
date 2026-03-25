package ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex

data class ProfileState(
    val isLoading: Boolean = true,
    val firstName: String = "",
    val lastName: String = "",
    val sex: UserSex? = null,
    val ageYears: Int = 0,
    val heightCm: Int = 0,
    val activityLevel: ActivityLevel = ActivityLevel.Moderate,
    val caloriesConsumed: Int = 0,
    val caloriesGoal: Int = 2000,
    val remainingCalories: Int = 2000,
    val initialWeightKg: Double = 70.0,
    val currentWeightKg: Double = 70.0,
    val targetWeightKg: Double = 65.0,
    val goalType: GoalType = GoalType.Maintain,
    val absoluteWeightChangeKg: Double = 0.0,
    val remainingToGoalKg: Double = 0.0,
    val progress: Float = 0f,
    val recommendedCalories: Int? = null,
    val recommendedProtein: Double? = null,
    val recommendedFat: Double? = null,
    val recommendedCarbs: Double? = null,
) : UiState

