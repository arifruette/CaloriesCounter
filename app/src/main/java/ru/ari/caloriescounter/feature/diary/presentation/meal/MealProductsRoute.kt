package ru.ari.caloriescounter.feature.diary.presentation.meal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.MealProductsViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsIntent

@Composable
fun MealProductsRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onNavigateToSearch: (MealType) -> Unit,
    viewModel: MealProductsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(MealProductsIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is MealProductsEffect.NavigateToSearch -> onNavigateToSearch(effect.mealType)
            }
        }
    }

    MealProductsScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onDeleteEntryClick = { entryId ->
            viewModel.onIntent(MealProductsIntent.DeleteEntryClicked(entryId))
        },
        onAddProductClick = {
            viewModel.onIntent(MealProductsIntent.AddProductClicked)
        },
    )
}
