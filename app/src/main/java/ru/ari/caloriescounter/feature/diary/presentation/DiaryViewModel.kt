package ru.ari.caloriescounter.feature.diary.presentation

import java.time.LocalDate
import java.util.Locale
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiEffect
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.diary.domain.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.DayDiary
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
) : BaseMviViewModel<DiaryIntent, DiaryState, DiaryEffect>(DiaryState()) {

    private var observeDiaryJob: Job? = null

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> observeDiary()
            is DiaryIntent.MealClicked -> navigateToMeal(intent.mealType)
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
}

data class DiaryState(
    val mealCards: List<MealCardUiModel> = MealType.entries.map { mealType ->
        MealCardUiModel(
            mealType = mealType,
            title = mealType.toTitle(),
            subtitle = "0 ккал · Б 0.0 Ж 0.0 У 0.0 · 0 блюд",
        )
    },
    val isLoading: Boolean = true,
) : UiState

data class MealCardUiModel(
    val mealType: MealType,
    val title: String,
    val subtitle: String,
)

sealed interface DiaryIntent : UiIntent {
    data object ScreenOpened : DiaryIntent
    data class MealClicked(val mealType: MealType) : DiaryIntent
}

sealed interface DiaryEffect : UiEffect {
    data class NavigateToMealProducts(val mealType: MealType) : DiaryEffect
}

private fun DayDiary.toMealCards(): List<MealCardUiModel> =
    MealType.entries.map { mealType ->
        val summary = mealSummaries[mealType]
        val calories = summary?.totalCalories ?: 0.0
        val protein = summary?.totalProtein ?: 0.0
        val fat = summary?.totalFat ?: 0.0
        val carbs = summary?.totalCarbs ?: 0.0
        val entriesCount = summary?.entriesCount ?: 0

        MealCardUiModel(
            mealType = mealType,
            title = mealType.toTitle(),
            subtitle = "${calories.toInt()} ккал · Б ${protein.ruOneDecimal()} Ж ${fat.ruOneDecimal()} У ${carbs.ruOneDecimal()} · $entriesCount блюд",
        )
    }

private fun MealType.toTitle(): String = when (this) {
    MealType.BREAKFAST -> "Завтрак"
    MealType.LUNCH -> "Обед"
    MealType.SNACK -> "Перекус"
}

private fun Double.ruOneDecimal(): String = String.format(Locale.forLanguageTag("ru-RU"), "%.1f", this)

