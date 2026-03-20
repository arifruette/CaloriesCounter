package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface ManualProductCreateIntent : UiIntent {
    data class NameChanged(val value: String) : ManualProductCreateIntent
    data class CaloriesChanged(val value: String) : ManualProductCreateIntent
    data class ProteinChanged(val value: String) : ManualProductCreateIntent
    data class FatChanged(val value: String) : ManualProductCreateIntent
    data class CarbsChanged(val value: String) : ManualProductCreateIntent
    data class GramsChanged(val value: String) : ManualProductCreateIntent
    data object SaveClicked : ManualProductCreateIntent
}
