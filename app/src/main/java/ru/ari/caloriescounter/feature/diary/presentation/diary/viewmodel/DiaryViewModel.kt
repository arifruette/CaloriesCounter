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
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DeletedMealPayload
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
        mealSummaries = emptyList(),
        totalCalories = 0.0,
        totalProtein = 0.0,
        totalFat = 0.0,
        totalCarbs = 0.0,
    )
    private var latestGoals = defaultNutritionGoals
    private var pendingDeletedMeal: DeletedMealPayload? = null

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> {
                observeDiary()
                observeNutritionGoals()
                observeWeightProfile()
                scheduleMidnightDateSwitch()
            }
            is DiaryIntent.MealClicked -> navigateToMeal(intent.mealKey, intent.mealTitle)
            DiaryIntent.NutritionGoalsClicked -> navigateToNutritionGoals()
            is DiaryIntent.NewMealTitleChanged -> updateState { copy(newMealTitleInput = intent.value) }
            DiaryIntent.AddMealClicked -> addMeal()
            is DiaryIntent.RenameMealClicked -> {
                updateState {
                    copy(
                        editingMealKey = intent.mealKey,
                        editingMealTitleInput = intent.currentTitle,
                    )
                }
            }
            is DiaryIntent.EditingMealTitleChanged -> updateState { copy(editingMealTitleInput = intent.value) }
            DiaryIntent.ConfirmRenameMealClicked -> confirmRenameMeal()
            DiaryIntent.DismissRenameMealClicked -> dismissRenameMeal()
            is DiaryIntent.DeleteMealClicked -> deleteMeal(intent.mealKey)
            DiaryIntent.UndoDeleteMealClicked -> undoDeleteMeal()
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

    private fun addMeal() {
        val title = state.value.newMealTitleInput.trim()
        if (title.isBlank()) return
        viewModelScope.launch {
            interactor.addCustomMealSlot(observedDate, title)
            updateState { copy(newMealTitleInput = "") }
        }
    }

    private fun confirmRenameMeal() {
        val mealKey = state.value.editingMealKey ?: return
        val title = state.value.editingMealTitleInput
        viewModelScope.launch {
            interactor.renameMealSlot(observedDate, mealKey, title)
            updateState {
                copy(
                    editingMealKey = null,
                    editingMealTitleInput = "",
                )
            }
        }
    }

    private fun dismissRenameMeal() {
        updateState {
            copy(
                editingMealKey = null,
                editingMealTitleInput = "",
            )
        }
    }

    private fun deleteMeal(mealKey: String) {
        viewModelScope.launch {
            pendingDeletedMeal = interactor.deleteMealSlotWithEntries(observedDate, mealKey)
            if (pendingDeletedMeal != null) {
                emitEffect(DiaryEffect.ShowUndoDeleteMeal)
            }
        }
    }

    private fun undoDeleteMeal() {
        val payload = pendingDeletedMeal ?: return
        viewModelScope.launch {
            interactor.restoreDeletedMeal(payload)
            pendingDeletedMeal = null
        }
    }

    private fun navigateToMeal(mealKey: String, mealTitle: String) {
        viewModelScope.launch {
            emitEffect(DiaryEffect.NavigateToMealProducts(mealKey = mealKey, mealTitle = mealTitle))
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
                    pendingDeletedMeal = null
                    updateState {
                        copy(
                            newMealTitleInput = "",
                            editingMealKey = null,
                            editingMealTitleInput = "",
                        )
                    }
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
    mealSummaries.map { summary ->
        MealCardUiModel(
            mealKey = summary.mealKey,
            title = summary.title,
            isBase = summary.mealKey == "breakfast" || summary.mealKey == "lunch" || summary.mealKey == "dinner",
            calories = summary.totalCalories.toInt(),
            protein = summary.totalProtein,
            fat = summary.totalFat,
            carbs = summary.totalCarbs,
            entriesCount = summary.entriesCount,
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

