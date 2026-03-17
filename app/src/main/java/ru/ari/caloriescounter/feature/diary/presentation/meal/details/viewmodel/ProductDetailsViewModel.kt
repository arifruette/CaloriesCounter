package ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract.ProductDetailsEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract.ProductDetailsIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract.ProductDetailsState

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryInteractor: DiaryInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<ProductDetailsIntent, ProductDetailsState, ProductDetailsEffect>(
        initialState = savedStateHandle.toInitialState(),
    ) {

    private val route = savedStateHandle.toRoute<AppRoute.MealProductDetailsRoute>()

    override fun onIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.GramsChanged -> onGramsChanged(intent.gramsInput)
            ProductDetailsIntent.AddClicked -> addProduct()
        }
    }

    private fun onGramsChanged(rawInput: String) {
        val parsedGrams = rawInput.parseGrams()
        val hasInputError = parsedGrams == null || parsedGrams <= 0.0
        updateState {
            copy(
                gramsInput = rawInput,
                hasInputError = hasInputError,
            ).recalculate(parsedGrams ?: 0.0)
        }
    }

    private fun addProduct() {
        val grams = state.value.gramsInput.parseGrams()
        if (grams == null || grams <= 0.0) {
            updateState { copy(hasInputError = true) }
            return
        }

        viewModelScope.launch {
            diaryInteractor.addEntry(
                DiaryEntry(
                    id = 0,
                    date = moscowDateTimeProvider.currentDate(),
                    mealType = route.mealType.toMealTypeOrDefault(),
                    product = ProductRef(
                        source = route.source.toProductSourceOrDefault(),
                        externalId = route.externalId,
                        barcode = route.barcode,
                        nameRu = route.nameRu,
                        nameOriginal = null,
                    ),
                    nutritionPer100g = NutritionPer100g(
                        calories = route.caloriesPer100g,
                        protein = route.proteinPer100g,
                        fat = route.fatPer100g,
                        carbs = route.carbsPer100g,
                    ),
                    portion = Portion(grams = grams),
                ),
            )
            emitEffect(ProductDetailsEffect.ProductAdded)
        }
    }
}

private fun SavedStateHandle.toInitialState(): ProductDetailsState {
    val route = toRoute<AppRoute.MealProductDetailsRoute>()
    return ProductDetailsState(
        mealType = route.mealType.toMealTypeOrDefault(),
        productName = route.nameRu,
        caloriesPer100g = route.caloriesPer100g,
        proteinPer100g = route.proteinPer100g,
        fatPer100g = route.fatPer100g,
        carbsPer100g = route.carbsPer100g,
    )
}

private fun ProductDetailsState.recalculate(grams: Double): ProductDetailsState {
    val multiplier = grams / 100.0
    return copy(
        calculatedCalories = (caloriesPer100g * multiplier).toInt(),
        calculatedProtein = proteinPer100g * multiplier,
        calculatedFat = fatPer100g * multiplier,
        calculatedCarbs = carbsPer100g * multiplier,
    )
}

private fun String.toMealTypeOrDefault(): MealType =
    runCatching { MealType.valueOf(this) }.getOrElse { MealType.BREAKFAST }

private fun String.toProductSourceOrDefault(): ProductSource =
    runCatching { ProductSource.valueOf(this) }.getOrElse { ProductSource.OPEN_FOOD_FACTS }

private fun String.parseGrams(): Double? = replace(',', '.').toDoubleOrNull()
