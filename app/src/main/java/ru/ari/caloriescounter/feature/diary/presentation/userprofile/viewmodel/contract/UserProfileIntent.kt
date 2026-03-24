package ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex

sealed interface UserProfileIntent : UiIntent {
    data object ScreenOpened : UserProfileIntent
    data class SexSelected(val value: UserSex) : UserProfileIntent
    data class AgeChanged(val value: String) : UserProfileIntent
    data class HeightChanged(val value: String) : UserProfileIntent
    data class CurrentWeightChanged(val value: String) : UserProfileIntent
    data class TargetWeightChanged(val value: String) : UserProfileIntent
    data class ActivityLevelSelected(val value: ActivityLevel) : UserProfileIntent
    data class GoalTypeSelected(val value: GoalType) : UserProfileIntent
    data object SaveClicked : UserProfileIntent
}

