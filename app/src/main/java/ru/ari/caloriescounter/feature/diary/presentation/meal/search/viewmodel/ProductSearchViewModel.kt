package ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.ManualProductInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.ProductSearchInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ManualProductUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchTab
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchState

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productSearchInteractor: ProductSearchInteractor,
    private val manualProductInteractor: ManualProductInteractor,
    private val diaryInteractor: DiaryInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<ProductSearchIntent, ProductSearchState, ProductSearchEffect>(ProductSearchState()) {

    private val route = savedStateHandle.toRoute<AppRoute.MealProductSearchRoute>()
    private val mealKey = route.mealKey
    private val mealTitle = route.mealTitle
    private var debounceSearchJob: Job? = null
    private var searchRequestJob: Job? = null

    init {
        observeManualProducts()
    }

    override fun onIntent(intent: ProductSearchIntent) {
        when (intent) {
            is ProductSearchIntent.TabSelected -> onTabSelected(intent.tab)
            is ProductSearchIntent.QueryChanged -> onQueryChanged(intent.query)
            ProductSearchIntent.SubmitSearch -> submitSearch(state.value.query.trim())
            is ProductSearchIntent.ProductClicked -> navigateToDetails(intent.product)
            is ProductSearchIntent.QuickAddClicked -> addProductWithDefaultPortion(intent.product)
            is ProductSearchIntent.ManualProductClicked -> navigateToDetails(intent.product.toSearchItem())
            is ProductSearchIntent.ManualProductQuickAddClicked -> addManualProductWithDefaultPortion(intent.product)
            is ProductSearchIntent.ManualProductDeleteClicked -> deleteManualProduct(intent.productId)
            ProductSearchIntent.CreateManualProductClicked -> navigateToManualCreate()
        }
    }

    private fun observeManualProducts() {
        viewModelScope.launch {
            manualProductInteractor.observeAll().collect { manualProducts ->
                updateState {
                    copy(
                        manualProducts = manualProducts.map { product ->
                            ManualProductUiModel(
                                id = product.id,
                                nameRu = product.nameRu,
                                caloriesPer100g = product.nutritionPer100g.calories,
                                proteinPer100g = product.nutritionPer100g.protein,
                                fatPer100g = product.nutritionPer100g.fat,
                                carbsPer100g = product.nutritionPer100g.carbs,
                            )
                        },
                    )
                }
            }
        }
    }

    private fun onTabSelected(tab: ProductSearchTab) {
        updateState { copy(selectedTab = tab) }
    }

    private fun onQueryChanged(query: String) {
        updateState { copy(query = query, hasError = false, errorMessageResId = null) }
        val normalizedQuery = query.trim()

        debounceSearchJob?.cancel()
        if (normalizedQuery.isBlank()) {
            searchRequestJob?.cancel()
            updateState { copy(results = emptyList(), isLoading = false, hasError = false, errorMessageResId = null) }
            return
        }

        debounceSearchJob = viewModelScope.launch {
            delay(400)
            submitSearch(normalizedQuery)
        }
    }

    private fun submitSearch(query: String) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            searchRequestJob?.cancel()
            updateState { copy(results = emptyList(), isLoading = false, hasError = false, errorMessageResId = null) }
            return
        }

        debounceSearchJob?.cancel()
        searchRequestJob?.cancel()
        searchRequestJob = viewModelScope.launch {
            updateState { copy(isLoading = true, hasError = false, errorMessageResId = null) }
            val result = runCatching { productSearchInteractor.searchByName(normalizedQuery) }
            if (normalizedQuery != state.value.query.trim()) return@launch
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
                if (error is CancellationException) throw error
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
            emitEffect(
                ProductSearchEffect.NavigateToProductDetails(
                    mealKey = mealKey,
                    mealTitle = mealTitle,
                    product = product,
                ),
            )
        }
    }

    private fun navigateToManualCreate() {
        viewModelScope.launch {
            emitEffect(
                ProductSearchEffect.NavigateToManualProductCreate(
                    mealKey = mealKey,
                    mealTitle = mealTitle,
                ),
            )
        }
    }

    private fun addProductWithDefaultPortion(product: ProductSearchItemUiModel) {
        if (state.value.quickAddInProgressKey != null) return
        val productKey = product.toStableKey()
        viewModelScope.launch {
            updateState { copy(quickAddInProgressKey = productKey) }
            runCatching {
                diaryInteractor.addEntry(
                    product.toDiaryEntry(
                        date = moscowDateTimeProvider.currentDate(),
                        mealKey = mealKey,
                        grams = DEFAULT_QUICK_ADD_GRAMS,
                    ),
                )
            }.onSuccess {
                emitEffect(ProductSearchEffect.ProductQuickAdded(productName = product.nameRu))
            }.onFailure {
                emitEffect(ProductSearchEffect.ShowMessage(R.string.meal_products_search_quick_add_error))
            }
            updateState { copy(quickAddInProgressKey = null) }
        }
    }

    private fun addManualProductWithDefaultPortion(product: ManualProductUiModel) {
        if (state.value.manualQuickAddInProgressId != null) return
        viewModelScope.launch {
            updateState { copy(manualQuickAddInProgressId = product.id) }
            runCatching {
                diaryInteractor.addEntry(
                    product.toSearchItem().toDiaryEntry(
                        date = moscowDateTimeProvider.currentDate(),
                        mealKey = mealKey,
                        grams = DEFAULT_QUICK_ADD_GRAMS,
                    ),
                )
            }.onSuccess {
                emitEffect(ProductSearchEffect.ProductQuickAdded(productName = product.nameRu))
            }.onFailure {
                emitEffect(ProductSearchEffect.ShowMessage(R.string.meal_products_search_quick_add_error))
            }
            updateState { copy(manualQuickAddInProgressId = null) }
        }
    }

    private fun deleteManualProduct(productId: Long) {
        if (state.value.manualDeleteInProgressId != null) return
        viewModelScope.launch {
            updateState { copy(manualDeleteInProgressId = productId) }
            runCatching {
                manualProductInteractor.delete(productId)
            }.onSuccess {
                emitEffect(ProductSearchEffect.ManualProductDeleted)
            }.onFailure {
                emitEffect(ProductSearchEffect.ShowMessage(R.string.meal_products_manual_delete_error))
            }
            updateState { copy(manualDeleteInProgressId = null) }
        }
    }
}

private fun ProductSearchItemUiModel.toStableKey(): String = "${source.name}:${externalId}:${nameRu}"

private fun ProductSearchItemUiModel.toDiaryEntry(
    date: java.time.LocalDate,
    mealKey: String,
    grams: Double,
): DiaryEntry =
    DiaryEntry(
        id = 0,
        date = date,
        mealKey = mealKey,
        product = ProductRef(
            source = source,
            externalId = externalId,
            barcode = barcode,
            nameRu = nameRu,
            nameOriginal = null,
        ),
        nutritionPer100g = NutritionPer100g(
            calories = caloriesPer100g,
            protein = proteinPer100g,
            fat = fatPer100g,
            carbs = carbsPer100g,
        ),
        portion = Portion(grams = grams),
    )

private fun ManualProductUiModel.toSearchItem(): ProductSearchItemUiModel =
    ProductSearchItemUiModel(
        source = ProductSource.MANUAL,
        externalId = id.toString(),
        barcode = null,
        nameRu = nameRu,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        carbsPer100g = carbsPer100g,
    )

private const val DEFAULT_QUICK_ADD_GRAMS = 100.0

