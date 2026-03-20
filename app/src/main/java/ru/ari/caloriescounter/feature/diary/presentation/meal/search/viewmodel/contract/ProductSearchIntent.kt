package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ManualProductUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchTab

sealed interface ProductSearchIntent : UiIntent {
    data class TabSelected(val tab: ProductSearchTab) : ProductSearchIntent
    data class QueryChanged(val query: String) : ProductSearchIntent
    data object SubmitSearch : ProductSearchIntent
    data class ProductClicked(val product: ProductSearchItemUiModel) : ProductSearchIntent
    data class QuickAddClicked(val product: ProductSearchItemUiModel) : ProductSearchIntent
    data class ManualProductClicked(val product: ManualProductUiModel) : ProductSearchIntent
    data class ManualProductQuickAddClicked(val product: ManualProductUiModel) : ProductSearchIntent
    data class ManualProductDeleteClicked(val productId: Long) : ProductSearchIntent
    data object CreateManualProductClicked : ProductSearchIntent
}
