package ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface ProductDetailsEffect : UiEffect {
    data object ProductAdded : ProductDetailsEffect
}
