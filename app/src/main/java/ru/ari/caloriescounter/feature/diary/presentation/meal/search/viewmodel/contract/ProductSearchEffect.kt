package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel

sealed interface ProductSearchEffect : UiEffect {
    data class NavigateToProductDetails(
        val mealKey: String,
        val mealTitle: String,
        val product: ProductSearchItemUiModel,
    ) : ProductSearchEffect

    data class ShowMessage(val messageResId: Int) : ProductSearchEffect
    data class ProductQuickAdded(val productName: String) : ProductSearchEffect
    data class NavigateToManualProductCreate(
        val mealKey: String,
        val mealTitle: String,
    ) : ProductSearchEffect
    data object ManualProductDeleted : ProductSearchEffect
}
