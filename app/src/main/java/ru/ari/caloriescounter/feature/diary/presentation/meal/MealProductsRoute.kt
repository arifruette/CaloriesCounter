package ru.ari.caloriescounter.feature.diary.presentation.meal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.MealProductsViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsIntent

@Composable
fun MealProductsRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onNavigateToSearch: (mealKey: String, mealTitle: String) -> Unit,
    onNavigateToEntryEdit: (entryId: Long, mealKey: String, mealTitle: String, entryName: String, grams: Double) -> Unit,
    viewModel: MealProductsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val deletedMessage = stringResource(R.string.meal_products_deleted)
    val undoAction = stringResource(R.string.action_undo)

    LaunchedEffect(viewModel) {
        viewModel.onIntent(MealProductsIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is MealProductsEffect.NavigateToSearch -> onNavigateToSearch(effect.mealKey, effect.mealTitle)
                is MealProductsEffect.NavigateToEntryEdit -> {
                    onNavigateToEntryEdit(
                        effect.entryId,
                        effect.mealKey,
                        effect.mealTitle,
                        effect.entryName,
                        effect.grams,
                    )
                }
                MealProductsEffect.ShowUndoDelete -> {
                    val result = snackbarHostState.showSnackbar(
                        message = deletedMessage,
                        actionLabel = undoAction,
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onIntent(MealProductsIntent.UndoDeleteClicked)
                    }
                }
            }
        }
    }

    MealProductsScreen(
        state = state.value,
        contentPadding = contentPadding,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onDeleteEntryClick = { entryId ->
            viewModel.onIntent(MealProductsIntent.DeleteEntryClicked(entryId))
        },
        onEntryClick = { entryId ->
            viewModel.onIntent(MealProductsIntent.EntryClicked(entryId))
        },
        onAddProductClick = {
            viewModel.onIntent(MealProductsIntent.AddProductClicked)
        },
    )
}

