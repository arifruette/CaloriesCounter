package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.diary.domain.interactor.NutritionGoalsInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsEffect
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsIntent
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsState

@HiltViewModel
class NutritionGoalsViewModel @Inject constructor(
    private val goalsInteractor: NutritionGoalsInteractor,
) : BaseMviViewModel<NutritionGoalsIntent, NutritionGoalsState, NutritionGoalsEffect>(NutritionGoalsState()) {

    private var observeGoalsJob: Job? = null

    override fun onIntent(intent: NutritionGoalsIntent) {
        when (intent) {
            NutritionGoalsIntent.ScreenOpened -> observeGoals()
            is NutritionGoalsIntent.CaloriesChanged -> updateState {
                copy(caloriesInput = intent.value, showValidationErrors = false)
            }
            is NutritionGoalsIntent.ProteinChanged -> updateState {
                copy(proteinInput = intent.value, showValidationErrors = false)
            }
            is NutritionGoalsIntent.FatChanged -> updateState {
                copy(fatInput = intent.value, showValidationErrors = false)
            }
            is NutritionGoalsIntent.CarbsChanged -> updateState {
                copy(carbsInput = intent.value, showValidationErrors = false)
            }
            NutritionGoalsIntent.SaveClicked -> saveGoals()
        }
    }

    private fun observeGoals() {
        if (observeGoalsJob != null) return

        observeGoalsJob = viewModelScope.launch {
            goalsInteractor.observeGoals().collect { goals ->
                updateState {
                    copy(
                        caloriesInput = goals.calories.toString(),
                        proteinInput = goals.protein.formatGoalInput(),
                        fatInput = goals.fat.formatGoalInput(),
                        carbsInput = goals.carbs.formatGoalInput(),
                        showValidationErrors = false,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun saveGoals() {
        val current = state.value
        val parsedGoals = current.toGoalsOrNull()
        if (parsedGoals == null) {
            updateState { copy(showValidationErrors = true) }
            return
        }

        viewModelScope.launch {
            goalsInteractor.updateGoals(parsedGoals)
            emitEffect(NutritionGoalsEffect.Saved)
        }
    }
}

private fun NutritionGoalsState.toGoalsOrNull(): NutritionGoals? {
    val calories = caloriesInput.toIntOrNull()?.takeIf { it > 0 } ?: return null
    val protein = proteinInput.toDoubleOrNull()?.takeIf { it > 0.0 } ?: return null
    val fat = fatInput.toDoubleOrNull()?.takeIf { it > 0.0 } ?: return null
    val carbs = carbsInput.toDoubleOrNull()?.takeIf { it > 0.0 } ?: return null

    return NutritionGoals(
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
    )
}

private fun Double.formatGoalInput(): String {
    val intValue = toInt()
    return if (this == intValue.toDouble()) intValue.toString() else toString()
}
