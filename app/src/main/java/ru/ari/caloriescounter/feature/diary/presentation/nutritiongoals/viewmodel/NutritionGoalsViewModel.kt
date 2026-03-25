package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.diary.domain.interactor.NutritionGoalsInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.UserProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionRecommendation
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsEffect
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsIntent
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsState

@HiltViewModel
class NutritionGoalsViewModel @Inject constructor(
    private val goalsInteractor: NutritionGoalsInteractor,
    private val userProfileInteractor: UserProfileInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
) : BaseMviViewModel<NutritionGoalsIntent, NutritionGoalsState, NutritionGoalsEffect>(NutritionGoalsState()) {

    private var observeGoalsJob: Job? = null
    private var observeRecommendationJob: Job? = null

    override fun onIntent(intent: NutritionGoalsIntent) {
        when (intent) {
            NutritionGoalsIntent.ScreenOpened -> {
                observeGoals()
                observeRecommendation()
            }
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
            NutritionGoalsIntent.ApplyRecommendationClicked -> applyRecommendation()
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

    private fun observeRecommendation() {
        if (observeRecommendationJob != null) return

        observeRecommendationJob = viewModelScope.launch {
            combine(
                userProfileInteractor.observeUserProfile(),
                weightProfileInteractor.observeWeightProfile(),
            ) { profile, weight ->
                if (profile == null) null else goalsInteractor.calculateRecommendation(profile, weight.currentWeightKg)
            }.collect { recommendation ->
                applyRecommendationToState(recommendation)
            }
        }
    }

    private fun applyRecommendationToState(recommendation: NutritionRecommendation?) {
        updateState {
            if (recommendation == null) {
                copy(
                    recommendedCalories = null,
                    recommendedProtein = null,
                    recommendedFat = null,
                    recommendedCarbs = null,
                )
            } else {
                copy(
                    recommendedCalories = recommendation.goals.calories,
                    recommendedProtein = recommendation.goals.protein,
                    recommendedFat = recommendation.goals.fat,
                    recommendedCarbs = recommendation.goals.carbs,
                )
            }
        }
    }

    private fun applyRecommendation() {
        val current = state.value
        val recommendedCalories = current.recommendedCalories ?: return
        val recommendedProtein = current.recommendedProtein ?: return
        val recommendedFat = current.recommendedFat ?: return
        val recommendedCarbs = current.recommendedCarbs ?: return

        updateState {
            copy(
                caloriesInput = recommendedCalories.toString(),
                proteinInput = recommendedProtein.formatGoalInput(),
                fatInput = recommendedFat.formatGoalInput(),
                carbsInput = recommendedCarbs.formatGoalInput(),
                showValidationErrors = false,
            )
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
