package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.net.SocketTimeoutException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.ProductSearchInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchState

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productSearchInteractor: ProductSearchInteractor,
) : BaseMviViewModel<ProductSearchIntent, ProductSearchState, ProductSearchEffect>(ProductSearchState()) {

    private val mealType = savedStateHandle.toRoute<AppRoute.MealProductSearchRoute>().mealType
        .toMealTypeOrDefault()
    private var debounceSearchJob: Job? = null

    override fun onIntent(intent: ProductSearchIntent) {
        when (intent) {
            is ProductSearchIntent.QueryChanged -> onQueryChanged(intent.query)
            ProductSearchIntent.SubmitSearch -> submitSearch(state.value.query.trim())
            is ProductSearchIntent.ProductClicked -> navigateToDetails(intent.product)
        }
    }

    private fun onQueryChanged(query: String) {
        updateState { copy(query = query, hasError = false, errorMessageResId = null) }
        val normalizedQuery = query.trim()

        debounceSearchJob?.cancel()
        if (normalizedQuery.isBlank()) {
            updateState { copy(results = emptyList(), isLoading = false, hasError = false, errorMessageResId = null) }
            return
        }

        debounceSearchJob = viewModelScope.launch {
            delay(400)
            submitSearch(normalizedQuery)
        }
    }

    private fun submitSearch(query: String) {
        if (query.isBlank()) {
            updateState { copy(results = emptyList(), isLoading = false, hasError = false, errorMessageResId = null) }
            return
        }

        debounceSearchJob?.cancel()
        viewModelScope.launch {
            updateState { copy(isLoading = true, hasError = false, errorMessageResId = null) }
            val result = runCatching { productSearchInteractor.searchByName(query) }
            result.onSuccess { products ->
                updateState {
                    copy(
                        isLoading = false,
                        hasError = false,
                        errorMessageResId = null,
                        results = products.map { product ->
                            ProductSearchItemUiModel(
                                source = product.source,
                                externalId = product.externalId,
                                barcode = product.barcode,
                                nameRu = product.displayNameRu,
                                caloriesPer100g = product.nutritionPer100g.calories,
                                proteinPer100g = product.nutritionPer100g.protein,
                                fatPer100g = product.nutritionPer100g.fat,
                                carbsPer100g = product.nutritionPer100g.carbs,
                            )
                        },
                    )
                }
            }.onFailure { error ->
                updateState {
                    copy(
                        isLoading = false,
                        results = emptyList(),
                        hasError = true,
                        errorMessageResId = if (error is SocketTimeoutException) {
                            R.string.meal_products_search_timeout
                        } else {
                            R.string.meal_products_search_error
                        },
                    )
                }
            }
        }
    }

    private fun navigateToDetails(product: ProductSearchItemUiModel) {
        viewModelScope.launch {
            emitEffect(ProductSearchEffect.NavigateToProductDetails(mealType = mealType, product = product))
        }
    }
}

private fun String.toMealTypeOrDefault(): MealType =
    runCatching { MealType.valueOf(this) }.getOrElse { MealType.BREAKFAST }
