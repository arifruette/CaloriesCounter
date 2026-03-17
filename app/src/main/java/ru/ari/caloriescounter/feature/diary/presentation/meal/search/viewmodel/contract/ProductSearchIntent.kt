package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel

sealed interface ProductSearchIntent : UiIntent {
    data class QueryChanged(val query: String) : ProductSearchIntent
    data object SubmitSearch : ProductSearchIntent
    data class ProductClicked(val product: ProductSearchItemUiModel) : ProductSearchIntent
}
