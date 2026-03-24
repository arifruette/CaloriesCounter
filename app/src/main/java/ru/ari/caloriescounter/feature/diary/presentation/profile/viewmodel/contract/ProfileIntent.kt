package ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface ProfileIntent : UiIntent {
    data object ScreenOpened : ProfileIntent
}

