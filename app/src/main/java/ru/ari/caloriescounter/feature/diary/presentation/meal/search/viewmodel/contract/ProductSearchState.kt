package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ManualProductUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchTab

data class ProductSearchState(
    val selectedTab: ProductSearchTab = ProductSearchTab.API,
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<ProductSearchItemUiModel> = emptyList(),
    val hasError: Boolean = false,
    val errorMessageResId: Int? = null,
    val quickAddInProgressKey: String? = null,
    val manualProducts: List<ManualProductUiModel> = emptyList(),
    val manualQuickAddInProgressId: Long? = null,
    val manualDeleteInProgressId: Long? = null,
) : UiState
