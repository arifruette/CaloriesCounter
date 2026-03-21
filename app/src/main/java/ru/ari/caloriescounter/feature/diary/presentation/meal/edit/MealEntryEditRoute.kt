package ru.ari.caloriescounter.feature.diary.presentation.meal.edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.MealEntryEditViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract.MealEntryEditEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract.MealEntryEditIntent

@Composable
fun MealEntryEditRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: MealEntryEditViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                MealEntryEditEffect.Saved -> onSaved()
            }
        }
    }

    MealEntryEditScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onGramsChanged = { value -> viewModel.onIntent(MealEntryEditIntent.GramsChanged(value)) },
        onSaveClick = { viewModel.onIntent(MealEntryEditIntent.SaveClicked) },
    )
}
