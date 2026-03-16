package ru.ari.caloriescounter.core.common.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent
import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

abstract class BaseMviViewModel<Intent : UiIntent, State : UiState, Effect : UiEffect>(
    initialState: State,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    abstract fun onIntent(intent: Intent)

    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    protected suspend fun emitEffect(effect: Effect) {
        _effects.send(effect)
    }
}
