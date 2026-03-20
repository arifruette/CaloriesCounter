package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.ManualProductInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductSource
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateState

@HiltViewModel
class ManualProductCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manualProductInteractor: ManualProductInteractor,
    private val diaryInteractor: DiaryInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<ManualProductCreateIntent, ManualProductCreateState, ManualProductCreateEffect>(
        ManualProductCreateState(),
    ) {

    private val mealType = savedStateHandle.toRoute<AppRoute.ManualProductCreateRoute>().mealType
        .toMealTypeOrDefault()

    override fun onIntent(intent: ManualProductCreateIntent) {
        when (intent) {
            is ManualProductCreateIntent.NameChanged -> updateState {
                copy(nameInput = intent.value, nameHasError = false)
            }

            is ManualProductCreateIntent.CaloriesChanged -> updateState {
                copy(caloriesInput = intent.value, caloriesHasError = false)
            }

            is ManualProductCreateIntent.ProteinChanged -> updateState {
                copy(proteinInput = intent.value, proteinHasError = false)
            }

            is ManualProductCreateIntent.FatChanged -> updateState {
                copy(fatInput = intent.value, fatHasError = false)
            }

            is ManualProductCreateIntent.CarbsChanged -> updateState {
                copy(carbsInput = intent.value, carbsHasError = false)
            }

            is ManualProductCreateIntent.GramsChanged -> updateState {
                copy(gramsInput = intent.value, gramsHasError = false)
            }

            ManualProductCreateIntent.SaveClicked -> saveManualProduct()
        }
    }

    private fun saveManualProduct() {
        val currentState = state.value
        if (currentState.isSaving) return

        val name = currentState.nameInput.trim()
        val calories = currentState.caloriesInput.parseDecimalInput()
        val protein = currentState.proteinInput.parseDecimalInput()
        val fat = currentState.fatInput.parseDecimalInput()
        val carbs = currentState.carbsInput.parseDecimalInput()
        val grams = currentState.gramsInput.parseDecimalInput()

        val hasNameError = name.isBlank()
        val hasCaloriesError = calories == null || calories < 0.0
        val hasProteinError = protein == null || protein < 0.0
        val hasFatError = fat == null || fat < 0.0
        val hasCarbsError = carbs == null || carbs < 0.0
        val hasGramsError = grams == null || grams <= 0.0
        val hasErrors = hasNameError || hasCaloriesError || hasProteinError || hasFatError || hasCarbsError || hasGramsError

        if (hasErrors) {
            updateState {
                copy(
                    nameHasError = hasNameError,
                    caloriesHasError = hasCaloriesError,
                    proteinHasError = hasProteinError,
                    fatHasError = hasFatError,
                    carbsHasError = hasCarbsError,
                    gramsHasError = hasGramsError,
                )
            }
            return
        }

        val nutrition = NutritionPer100g(
            calories = calories ?: 0.0,
            protein = protein ?: 0.0,
            fat = fat ?: 0.0,
            carbs = carbs ?: 0.0,
        )

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            runCatching {
                val manualProductId = manualProductInteractor.add(
                    nameRu = name,
                    nutritionPer100g = nutrition,
                )
                diaryInteractor.addEntry(
                    DiaryEntry(
                        id = 0,
                        date = moscowDateTimeProvider.currentDate(),
                        mealType = mealType,
                        product = ProductRef(
                            source = ProductSource.MANUAL,
                            externalId = manualProductId.toString(),
                            barcode = null,
                            nameRu = name,
                            nameOriginal = null,
                        ),
                        nutritionPer100g = nutrition,
                        portion = Portion(grams = grams ?: 0.0),
                    ),
                )
            }.onSuccess {
                emitEffect(ManualProductCreateEffect.Saved)
            }.onFailure {
                emitEffect(ManualProductCreateEffect.ShowMessage(R.string.meal_products_manual_add_error))
            }
            updateState { copy(isSaving = false) }
        }
    }
}

private fun String.toMealTypeOrDefault(): MealType =
    runCatching { MealType.valueOf(this) }.getOrElse { MealType.BREAKFAST }

private fun String.parseDecimalInput(): Double? = trim().replace(',', '.').toDoubleOrNull()
