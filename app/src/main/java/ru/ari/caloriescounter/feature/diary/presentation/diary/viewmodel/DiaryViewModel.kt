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
import ru.ari.caloriescounter.feature.diary.domain.interactor.NutritionGoalsInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryEffect
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryIntent
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryState
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.DiaryMacroProgressUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.DiaryNutritionProgressCardUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.MealCardUiModel

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
    private val nutritionGoalsInteractor: NutritionGoalsInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<DiaryIntent, DiaryState, DiaryEffect>(DiaryState()) {

    private var observeDiaryJob: Job? = null
    private var observeGoalsJob: Job? = null
    private var observeWeightJob: Job? = null
    private var midnightUpdateJob: Job? = null
    private var observedDate = moscowDateTimeProvider.currentDate()
    private var latestDayDiary = DayDiary(
        date = observedDate,
        entries = emptyList(),
        mealSummaries = emptyMap(),
        totalCalories = 0.0,
        totalProtein = 0.0,
        totalFat = 0.0,
        totalCarbs = 0.0,
    )
    private var latestGoals = defaultNutritionGoals

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> {
                observeDiary()
                observeNutritionGoals()
                observeWeightProfile()
                scheduleMidnightDateSwitch()
            }
            is DiaryIntent.MealClicked -> navigateToMeal(intent.mealType)
            DiaryIntent.NutritionGoalsClicked -> navigateToNutritionGoals()
            DiaryIntent.DecreaseCurrentWeight -> updateCurrentWeight(-WEIGHT_STEP_KG)
            DiaryIntent.IncreaseCurrentWeight -> updateCurrentWeight(WEIGHT_STEP_KG)
            DiaryIntent.DecreaseCurrentWeightFast -> updateCurrentWeight(-WEIGHT_LONG_PRESS_STEP_KG)
            DiaryIntent.IncreaseCurrentWeightFast -> updateCurrentWeight(WEIGHT_LONG_PRESS_STEP_KG)
        }
    }

    private fun observeDiary() {
        observeDiaryJob?.cancel()

        observeDiaryJob = viewModelScope.launch {
            interactor.observeDiary(observedDate).collect { dayDiary ->
                latestDayDiary = dayDiary
                updateState {
                    copy(
                        nutritionProgressCard = buildNutritionProgressCard(
                            dayDiary = latestDayDiary,
                            goals = latestGoals,
                        ),
                        mealCards = dayDiary.toMealCards(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun observeNutritionGoals() {
        if (observeGoalsJob != null) return

        observeGoalsJob = viewModelScope.launch {
            nutritionGoalsInteractor.observeGoals().collect { goals ->
                latestGoals = goals
                updateState {
                    copy(
                        nutritionProgressCard = buildNutritionProgressCard(
                            dayDiary = latestDayDiary,
                            goals = latestGoals,
                        ),
                    )
                }
            }
        }
    }

    private fun observeWeightProfile() {
        if (observeWeightJob != null) return

        observeWeightJob = viewModelScope.launch {
            weightProfileInteractor.observeWeightProfile().collect { profile ->
                updateState {
                    copy(
                        currentWeightKg = profile.currentWeightKg,
                        targetWeightKg = profile.targetWeightKg,
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

    private fun navigateToNutritionGoals() {
        viewModelScope.launch {
            emitEffect(DiaryEffect.NavigateToNutritionGoals)
        }
    }

    private fun updateCurrentWeight(delta: Double) {
        val updatedWeight = state.value.currentWeightKg + delta
        viewModelScope.launch {
            weightProfileInteractor.updateCurrentWeight(updatedWeight)
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

private val defaultNutritionGoals = NutritionGoals(
    calories = 2000,
    protein = 100.0,
    fat = 70.0,
    carbs = 250.0,
)

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

private fun buildNutritionProgressCard(
    dayDiary: DayDiary,
    goals: NutritionGoals,
): DiaryNutritionProgressCardUiModel =
    DiaryNutritionProgressCardUiModel(
        calories = DiaryMacroProgressUiModel(
            current = dayDiary.totalCalories,
            goal = goals.calories.toDouble(),
        ),
        protein = DiaryMacroProgressUiModel(
            current = dayDiary.totalProtein,
            goal = goals.protein,
        ),
        fat = DiaryMacroProgressUiModel(
            current = dayDiary.totalFat,
            goal = goals.fat,
        ),
        carbs = DiaryMacroProgressUiModel(
            current = dayDiary.totalCarbs,
            goal = goals.carbs,
        ),
    )
