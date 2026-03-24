package ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.NutritionGoalsInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.UserProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile
import ru.ari.caloriescounter.feature.diary.domain.model.weight.WeightProfile
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract.ProfileEffect
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract.ProfileIntent
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract.ProfileState
import kotlin.math.abs

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileInteractor: UserProfileInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
    private val nutritionGoalsInteractor: NutritionGoalsInteractor,
    private val diaryInteractor: DiaryInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<ProfileIntent, ProfileState, ProfileEffect>(ProfileState()) {

    private var observeUserProfileJob: Job? = null
    private var observeWeightJob: Job? = null
    private var observeGoalsJob: Job? = null
    private var observeDiaryJob: Job? = null
    private var midnightUpdateJob: Job? = null

    private var observedDate = moscowDateTimeProvider.currentDate()
    private var latestUserProfile: UserProfile? = null
    private var latestWeightProfile = defaultWeightProfile
    private var latestGoals = defaultNutritionGoals
    private var latestDayDiary = emptyDayDiary(observedDate)

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.ScreenOpened -> observeData()
        }
    }

    private fun observeData() {
        observeUserProfile()
        observeWeightProfile()
        observeNutritionGoals()
        observeDiaryForDate()
        scheduleMidnightDateSwitch()
    }

    private fun observeUserProfile() {
        if (observeUserProfileJob != null) return

        observeUserProfileJob = viewModelScope.launch {
            userProfileInteractor.observeUserProfile().collect { profile ->
                latestUserProfile = profile
                applyState()
            }
        }
    }

    private fun observeWeightProfile() {
        if (observeWeightJob != null) return

        observeWeightJob = viewModelScope.launch {
            weightProfileInteractor.observeWeightProfile().collect { profile ->
                latestWeightProfile = profile
                applyState()
            }
        }
    }

    private fun observeNutritionGoals() {
        if (observeGoalsJob != null) return

        observeGoalsJob = viewModelScope.launch {
            nutritionGoalsInteractor.observeGoals().collect { goals ->
                latestGoals = goals
                applyState()
            }
        }
    }

    private fun observeDiaryForDate() {
        observeDiaryJob?.cancel()
        observeDiaryJob = viewModelScope.launch {
            diaryInteractor.observeDiary(observedDate).collect { dayDiary ->
                latestDayDiary = dayDiary
                applyState()
            }
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
                    latestDayDiary = emptyDayDiary(newDate)
                    observeDiaryForDate()
                    applyState()
                }
            }
        }
    }

    private fun applyState() {
        val currentWeight = latestWeightProfile.currentWeightKg
        val initialWeight = latestWeightProfile.initialWeightKg
        val targetWeight = latestWeightProfile.targetWeightKg
        val goalType = latestUserProfile?.goalType ?: GoalType.Maintain
        val caloriesGoal = latestGoals.calories
        val caloriesConsumed = latestDayDiary.totalCalories.toInt()
        val remainingCalories = (caloriesGoal - caloriesConsumed).coerceAtLeast(0)

        updateState {
            copy(
                isLoading = false,
                firstName = latestUserProfile?.firstName.orEmpty(),
                lastName = latestUserProfile?.lastName.orEmpty(),
                sex = latestUserProfile?.sex,
                ageYears = latestUserProfile?.ageYears ?: 0,
                heightCm = latestUserProfile?.heightCm ?: 0,
                activityLevel = latestUserProfile?.activityLevel ?: state.value.activityLevel,
                caloriesConsumed = caloriesConsumed,
                caloriesGoal = caloriesGoal,
                remainingCalories = remainingCalories,
                initialWeightKg = initialWeight,
                currentWeightKg = currentWeight,
                targetWeightKg = targetWeight,
                goalType = goalType,
                absoluteWeightChangeKg = abs(currentWeight - initialWeight),
                remainingToGoalKg = goalType.remainingToGoal(currentWeight, targetWeight),
                progress = calculateProgress(
                    initialWeightKg = initialWeight,
                    currentWeightKg = currentWeight,
                    targetWeightKg = targetWeight,
                ),
            )
        }
    }
}

private fun calculateProgress(
    initialWeightKg: Double,
    currentWeightKg: Double,
    targetWeightKg: Double,
): Float {
    val total = abs(targetWeightKg - initialWeightKg)
    if (total < 0.0001) {
        return if (abs(currentWeightKg - targetWeightKg) < 0.0001) 1f else 0f
    }

    return (abs(currentWeightKg - initialWeightKg) / total).toFloat().coerceIn(0f, 1f)
}

private fun GoalType.remainingToGoal(currentWeightKg: Double, targetWeightKg: Double): Double =
    when (this) {
        GoalType.Lose -> (currentWeightKg - targetWeightKg).coerceAtLeast(0.0)
        GoalType.Gain -> (targetWeightKg - currentWeightKg).coerceAtLeast(0.0)
        GoalType.Maintain -> 0.0
    }

private fun emptyDayDiary(date: LocalDate): DayDiary =
    DayDiary(
        date = date,
        entries = emptyList(),
        mealSummaries = emptyMap(),
        totalCalories = 0.0,
        totalProtein = 0.0,
        totalFat = 0.0,
        totalCarbs = 0.0,
    )

private val defaultNutritionGoals = NutritionGoals(
    calories = 2000,
    protein = 100.0,
    fat = 70.0,
    carbs = 250.0,
)

private val defaultWeightProfile = WeightProfile(
    initialWeightKg = 70.0,
    currentWeightKg = 70.0,
    targetWeightKg = 65.0,
)

