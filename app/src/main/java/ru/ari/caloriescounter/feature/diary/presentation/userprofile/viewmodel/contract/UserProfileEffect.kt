package ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface UserProfileEffect : UiEffect {
    data object Saved : UserProfileEffect
}

