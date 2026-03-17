package ru.ari.caloriescounter.feature.diary.presentation.meal.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.ProductSearchViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchIntent

@Composable
fun ProductSearchRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onNavigateToProductDetails: (MealType, ProductSearchItemUiModel) -> Unit,
    viewModel: ProductSearchViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is ProductSearchEffect.NavigateToProductDetails ->
                    onNavigateToProductDetails(effect.mealType, effect.product)
            }
        }
    }

    ProductSearchScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onQueryChanged = { viewModel.onIntent(ProductSearchIntent.QueryChanged(it)) },
        onSearchSubmit = { viewModel.onIntent(ProductSearchIntent.SubmitSearch) },
        onProductClick = { product -> viewModel.onIntent(ProductSearchIntent.ProductClicked(product)) },
    )
}
