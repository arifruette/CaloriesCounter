package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel

sealed interface ProductSearchEffect : UiEffect {
    data class NavigateToProductDetails(
        val mealType: MealType,
        val product: ProductSearchItemUiModel,
    ) : ProductSearchEffect
}
