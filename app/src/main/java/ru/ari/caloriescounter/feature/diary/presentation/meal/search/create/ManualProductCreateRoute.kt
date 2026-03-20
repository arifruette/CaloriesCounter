package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.ManualProductCreateViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateIntent

@Composable
fun ManualProductCreateRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ManualProductCreateViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                ManualProductCreateEffect.Saved -> onSaved()
                is ManualProductCreateEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(context.getString(effect.messageResId))
            }
        }
    }

    ManualProductCreateScreen(
        state = state.value,
        contentPadding = contentPadding,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onNameChanged = { viewModel.onIntent(ManualProductCreateIntent.NameChanged(it)) },
        onCaloriesChanged = { viewModel.onIntent(ManualProductCreateIntent.CaloriesChanged(it)) },
        onProteinChanged = { viewModel.onIntent(ManualProductCreateIntent.ProteinChanged(it)) },
        onFatChanged = { viewModel.onIntent(ManualProductCreateIntent.FatChanged(it)) },
        onCarbsChanged = { viewModel.onIntent(ManualProductCreateIntent.CarbsChanged(it)) },
        onGramsChanged = { viewModel.onIntent(ManualProductCreateIntent.GramsChanged(it)) },
        onSaveClick = { viewModel.onIntent(ManualProductCreateIntent.SaveClicked) },
    )
}
