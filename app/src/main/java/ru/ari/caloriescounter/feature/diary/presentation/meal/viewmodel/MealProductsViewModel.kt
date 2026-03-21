package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.time.MoscowDateTimeProvider
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.diary.DiaryEntry
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.meal.model.MealEntryUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsState

@HiltViewModel
class MealProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryInteractor: DiaryInteractor,
    private val moscowDateTimeProvider: MoscowDateTimeProvider,
) : BaseMviViewModel<MealProductsIntent, MealProductsState, MealProductsEffect>(
        initialState = MealProductsState(
            mealType = savedStateHandle.toRoute<AppRoute.MealProductsRoute>().mealType.toMealTypeOrDefault(),
        ),
    ) {

    private var observeEntriesJob: Job? = null
    private var midnightUpdateJob: Job? = null
    private var observedDate = moscowDateTimeProvider.currentDate()
    private var latestEntries: List<DiaryEntry> = emptyList()
    private var pendingDeletedEntry: DiaryEntry? = null

    override fun onIntent(intent: MealProductsIntent) {
        when (intent) {
            MealProductsIntent.ScreenOpened -> {
                observeEntries()
                scheduleMidnightDateSwitch()
            }
            is MealProductsIntent.EntryClicked -> navigateToEntryEdit(intent.entryId)
            is MealProductsIntent.DeleteEntryClicked -> deleteEntry(intent.entryId)
            MealProductsIntent.UndoDeleteClicked -> undoDelete()
            MealProductsIntent.AddProductClicked -> navigateToSearch()
        }
    }

    private fun observeEntries() {
        observeEntriesJob?.cancel()
        observeEntriesJob = viewModelScope.launch {
            diaryInteractor.observeMealEntries(observedDate, state.value.mealType).collect { entries ->
                latestEntries = entries
                val uiEntries = entries.map { it.toUiModel() }
                updateState {
                    copy(
                        isLoading = false,
                        entries = uiEntries,
                        totalCalories = entries.sumOf { it.totalCalories() }.toInt(),
                        totalProtein = entries.sumOf { it.totalProtein() },
                        totalFat = entries.sumOf { it.totalFat() },
                        totalCarbs = entries.sumOf { it.totalCarbs() },
                    )
                }
            }
        }
    }

    private fun deleteEntry(entryId: Long) {
        viewModelScope.launch {
            pendingDeletedEntry = latestEntries.firstOrNull { it.id == entryId }
            diaryInteractor.removeEntry(entryId)
            if (pendingDeletedEntry != null) {
                emitEffect(MealProductsEffect.ShowUndoDelete)
            }
        }
    }

    private fun undoDelete() {
        val entryToRestore = pendingDeletedEntry ?: return
        viewModelScope.launch {
            diaryInteractor.addEntry(entryToRestore)
            pendingDeletedEntry = null
        }
    }

    private fun navigateToSearch() {
        viewModelScope.launch {
            emitEffect(MealProductsEffect.NavigateToSearch(state.value.mealType))
        }
    }

    private fun navigateToEntryEdit(entryId: Long) {
        val entry = latestEntries.firstOrNull { it.id == entryId } ?: return
        viewModelScope.launch {
            emitEffect(
                MealProductsEffect.NavigateToEntryEdit(
                    entryId = entry.id,
                    mealType = state.value.mealType,
                    entryName = entry.product.nameRu,
                    grams = entry.portion.grams,
                ),
            )
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
                    observeEntries()
                }
            }
        }
    }
}

private fun String.toMealTypeOrDefault(): MealType =
    runCatching { MealType.valueOf(this) }.getOrElse { MealType.BREAKFAST }

private fun DiaryEntry.toUiModel(): MealEntryUiModel =
    MealEntryUiModel(
        id = id,
        name = product.nameRu,
        grams = portion.grams,
        calories = totalCalories().toInt(),
        protein = totalProtein(),
        fat = totalFat(),
        carbs = totalCarbs(),
    )
