package ru.ari.caloriescounter.feature.recipes.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.recipes.domain.interactor.RecipesInteractor
import ru.ari.caloriescounter.feature.recipes.presentation.viewmodel.contract.RecipesEffect
import ru.ari.caloriescounter.feature.recipes.presentation.viewmodel.contract.RecipesIntent
import ru.ari.caloriescounter.feature.recipes.presentation.viewmodel.contract.RecipesState

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val interactor: RecipesInteractor,
) : BaseMviViewModel<RecipesIntent, RecipesState, RecipesEffect>(RecipesState()) {

    override fun onIntent(intent: RecipesIntent) {
        when (intent) {
            RecipesIntent.ScreenOpened -> viewModelScope.launch {
                updateState { copy(isReady = true) }
            }
        }
    }
}
