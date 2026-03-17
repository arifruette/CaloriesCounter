package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel

data class ProductSearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<ProductSearchItemUiModel> = emptyList(),
    val hasError: Boolean = false,
) : UiState
