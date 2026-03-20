package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface ManualProductCreateEffect : UiEffect {
    data object Saved : ManualProductCreateEffect
    data class ShowMessage(val messageResId: Int) : ManualProductCreateEffect
}
