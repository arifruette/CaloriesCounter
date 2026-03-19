package ru.ari.caloriescounter.feature.stats.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.feature.stats.domain.interactor.StatsInteractor
import ru.ari.caloriescounter.feature.stats.domain.model.WeeklySummary
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsEffect
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsIntent
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsState
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.model.DailyCaloriePointUiModel
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.model.WeeklySummaryUiModel

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val interactor: StatsInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<StatsIntent, StatsState, StatsEffect>(StatsState()) {

    private var observeSummaryJob: Job? = null
    private var midnightUpdateJob: Job? = null
    private var observedDate = moscowDateTimeProvider.currentDate()

    override fun onIntent(intent: StatsIntent) {
        when (intent) {
            StatsIntent.ScreenOpened -> startObservingSummary()
        }
    }

    private fun startObservingSummary() {
        if (observeSummaryJob != null) return

        observeSummary()
        scheduleMidnightDateSwitch()
    }

    private fun observeSummary() {
        observeSummaryJob?.cancel()
        observeSummaryJob = viewModelScope.launch {
            updateState { copy(isLoading = true) }
            interactor.observeWeeklySummary(endDate = observedDate).collect { summary ->
                updateState {
                    copy(
                        isLoading = false,
                        weeklySummary = summary.toUiModel(),
                        isEmpty = summary.loggedDaysInWindow == 0,
                    )
                }
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
                    observeSummary()
                }
            }
        }
    }
}

private fun WeeklySummary.toUiModel(): WeeklySummaryUiModel =
    WeeklySummaryUiModel(
        dailyCalories = dailyCalories
            .sortedBy { it.date.dayOfWeek.value }
            .map { point ->
            DailyCaloriePointUiModel(
                dayLabel = point.date.format(WEEKDAY_FORMATTER).replaceFirstChar { char ->
                    if (char.isLowerCase()) {
                        char.titlecase(WEEKDAY_LOCALE)
                    } else {
                        char.toString()
                    }
                },
                calories = point.calories.roundToInt(),
                isGoalCompleted = point.isGoalCompleted,
            )
        },
        averageCaloriesPerDay = averageCaloriesPerDay.roundToInt(),
        goalCompletedDays = goalCompletedDays,
        averageProteinPerDay = roundToSingleDecimal(averageProteinPerDay),
        bestStreakDays = bestStreakDays,
        currentStreakDays = currentStreakDays,
        loggedDaysInWindow = loggedDaysInWindow,
    )

private fun roundToSingleDecimal(value: Double): Double =
    kotlin.math.round(value * 10.0) / 10.0

private val WEEKDAY_LOCALE: Locale = Locale.forLanguageTag("ru-RU")
private val WEEKDAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EE", WEEKDAY_LOCALE)
