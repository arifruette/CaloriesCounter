package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryEffect
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryIntent
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryState
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.MealCardUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.WeightCardUiModel

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<DiaryIntent, DiaryState, DiaryEffect>(DiaryState()) {

    private var observeDiaryJob: Job? = null
    private var observeWeightJob: Job? = null
    private var midnightUpdateJob: Job? = null
    private var observedDate = moscowDateTimeProvider.currentDate()

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> {
                observeDiary()
                observeWeightProfile()
                scheduleMidnightDateSwitch()
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
        observeDiaryJob?.cancel()

        observeDiaryJob = viewModelScope.launch {
            interactor.observeDiary(observedDate).collect { dayDiary ->
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

    private fun scheduleMidnightDateSwitch() {
        if (midnightUpdateJob != null) return

        midnightUpdateJob = viewModelScope.launch {
            while (isActive) {
                delay(moscowDateTimeProvider.millisUntilNextMidnight())
                val newDate = moscowDateTimeProvider.currentDate()
                if (newDate != observedDate) {
                    observedDate = newDate
                    observeDiary()
                }
            }
        }
    }
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
