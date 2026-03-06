package ru.ari.caloriescounter.feature.diary.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiEffect
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.diary.domain.DiaryInteractor

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
) : BaseMviViewModel<DiaryIntent, DiaryState, DiaryEffect>(DiaryState()) {

    override fun onIntent(intent: DiaryIntent) {
        when (intent) {
            DiaryIntent.ScreenOpened -> viewModelScope.launch {
                emitEffect(DiaryEffect.Ready)
            }
        }
    }
}

data class DiaryState(
    val title: String = "Diary",
) : UiState

sealed interface DiaryIntent : UiIntent {
    data object ScreenOpened : DiaryIntent
}

sealed interface DiaryEffect : UiEffect {
    data object Ready : DiaryEffect
}
