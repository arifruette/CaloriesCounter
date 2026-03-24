package ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.navigation.AppRoute
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract.MealEntryEditEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract.MealEntryEditIntent
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract.MealEntryEditState

@HiltViewModel
class MealEntryEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryInteractor: DiaryInteractor,
) : BaseMviViewModel<MealEntryEditIntent, MealEntryEditState, MealEntryEditEffect>(
        initialState = savedStateHandle.toInitialState(),
    ) {

    override fun onIntent(intent: MealEntryEditIntent) {
        when (intent) {
            is MealEntryEditIntent.GramsChanged -> onGramsChanged(intent.gramsInput)
            MealEntryEditIntent.SaveClicked -> save()
        }
    }

    private fun onGramsChanged(gramsInput: String) {
        updateState {
            copy(
                gramsInput = gramsInput,
                hasInputError = false,
            )
        }
    }

    private fun save() {
        val grams = state.value.gramsInput.parseGrams()
        if (grams == null || grams <= 0.0) {
            updateState { copy(hasInputError = true) }
            return
        }
        viewModelScope.launch {
            diaryInteractor.updateEntryPortion(
                entryId = state.value.entryId,
                grams = grams,
            )
            emitEffect(MealEntryEditEffect.Saved)
        }
    }
}

private fun SavedStateHandle.toInitialState(): MealEntryEditState {
    val route = toRoute<AppRoute.MealEntryEditRoute>()
    return MealEntryEditState(
        entryId = route.entryId,
        mealKey = route.mealKey,
        mealTitle = route.mealTitle,
        entryName = route.entryName,
        gramsInput = route.grams.formatRuDecimal(),
    )
}

private fun String.parseGrams(): Double? = replace(',', '.').toDoubleOrNull()

