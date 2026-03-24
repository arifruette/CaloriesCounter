package ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex

data class UserProfileState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val sex: UserSex? = null,
    val ageInput: String = "",
    val heightInput: String = "",
    val currentWeightInput: String = "",
    val targetWeightInput: String = "",
    val activityLevel: ActivityLevel = ActivityLevel.Moderate,
    val goalType: GoalType = GoalType.Maintain,
    val showValidationErrors: Boolean = false,
) : UiState

