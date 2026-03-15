package ru.ari.caloriescounter.feature.diary.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiEffect
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.diary.domain.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
) : BaseMviViewModel<DiaryIntent, DiaryState, DiaryEffect>(DiaryState()) {

    private var observeDiaryJob: Job? = null
    private var observeWeightJob: Job? = null

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> {
                observeDiary()
                observeWeightProfile()
            }
            is DiaryIntent.MealClicked -> navigateToMeal(intent.mealType)
            DiaryIntent.DecreaseCurrentWeight -> updateCurrentWeight(-WEIGHT_STEP_KG)
            DiaryIntent.IncreaseCurrentWeight -> updateCurrentWeight(WEIGHT_STEP_KG)
            DiaryIntent.DecreaseCurrentWeightFast -> updateCurrentWeight(-WEIGHT_LONG_PRESS_STEP_KG)
            DiaryIntent.IncreaseCurrentWeightFast -> updateCurrentWeight(WEIGHT_LONG_PRESS_STEP_KG)
            DiaryIntent.WeightCardClicked -> navigateToWeightGoal()
        }
    }

    private fun observeDiary() {
        if (observeDiaryJob != null) return

        observeDiaryJob = viewModelScope.launch {
            interactor.observeDiary(LocalDate.now()).collect { dayDiary ->
                updateState {
                    copy(
                        mealCards = dayDiary.toMealCards(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun navigateToMeal(mealType: MealType) {
        viewModelScope.launch {
            emitEffect(DiaryEffect.NavigateToMealProducts(mealType))
        }
    }

    private fun observeWeightProfile() {
        if (observeWeightJob != null) return

        observeWeightJob = viewModelScope.launch {
            weightProfileInteractor.observeWeightProfile().collect { profile ->
                updateState {
                    copy(
                        weightCard = WeightCardUiModel(
                            currentWeightKg = profile.currentWeightKg,
                            targetWeightKg = profile.targetWeightKg,
                        ),
                    )
                }
            }
        }
    }

    private fun updateCurrentWeight(delta: Double) {
        val updatedWeight = state.value.weightCard.currentWeightKg + delta
        viewModelScope.launch {
            weightProfileInteractor.updateCurrentWeight(updatedWeight)
        }
    }

    private fun navigateToWeightGoal() {
        viewModelScope.launch {
            emitEffect(DiaryEffect.NavigateToWeightGoal)
        }
    }
}

data class DiaryState(
    val weightCard: WeightCardUiModel = WeightCardUiModel(),
    val mealCards: List<MealCardUiModel> = MealType.entries.map { mealType ->
        MealCardUiModel(mealType = mealType)
    },
    val isLoading: Boolean = true,
) : UiState

data class MealCardUiModel(
    val mealType: MealType,
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val entriesCount: Int = 0,
)

data class WeightCardUiModel(
    val currentWeightKg: Double = 70.0,
    val targetWeightKg: Double = 65.0,
)

sealed interface DiaryIntent : UiIntent {
    data object ScreenOpened : DiaryIntent
    data class MealClicked(val mealType: MealType) : DiaryIntent
    data object DecreaseCurrentWeight : DiaryIntent
    data object IncreaseCurrentWeight : DiaryIntent
    data object DecreaseCurrentWeightFast : DiaryIntent
    data object IncreaseCurrentWeightFast : DiaryIntent
    data object WeightCardClicked : DiaryIntent
}

sealed interface DiaryEffect : UiEffect {
    data class NavigateToMealProducts(val mealType: MealType) : DiaryEffect
    data object NavigateToWeightGoal : DiaryEffect
}

private const val WEIGHT_STEP_KG = 0.1
private const val WEIGHT_LONG_PRESS_STEP_KG = 1.0

private fun DayDiary.toMealCards(): List<MealCardUiModel> =
    MealType.entries.map { mealType ->
        val summary = mealSummaries[mealType]

        MealCardUiModel(
            mealType = mealType,
            calories = (summary?.totalCalories ?: 0.0).toInt(),
            protein = summary?.totalProtein ?: 0.0,
            fat = summary?.totalFat ?: 0.0,
            carbs = summary?.totalCarbs ?: 0.0,
            entriesCount = summary?.entriesCount ?: 0,
        )
    }
