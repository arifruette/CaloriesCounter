package ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface ProductDetailsIntent : UiIntent {
    data class GramsChanged(val gramsInput: String) : ProductDetailsIntent
    data object AddClicked : ProductDetailsIntent
}
