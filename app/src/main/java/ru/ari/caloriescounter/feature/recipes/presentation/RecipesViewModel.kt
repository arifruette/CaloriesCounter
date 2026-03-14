package ru.ari.caloriescounter.feature.recipes.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiEffect
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.recipes.domain.RecipesInteractor

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val interactor: RecipesInteractor,
) : BaseMviViewModel<RecipesIntent, RecipesState, RecipesEffect>(RecipesState()) {

    override fun onIntent(intent: RecipesIntent) {
        when (intent) {
            RecipesIntent.ScreenOpened -> viewModelScope.launch {
                emitEffect(RecipesEffect.Ready)
            }
        }
    }
}

data class RecipesState(
    val title: String = "Р РµС†РµРїС‚С‹",
) : UiState

sealed interface RecipesIntent : UiIntent {
    data object ScreenOpened : RecipesIntent
}

sealed interface RecipesEffect : UiEffect {
    data object Ready : RecipesEffect
}

