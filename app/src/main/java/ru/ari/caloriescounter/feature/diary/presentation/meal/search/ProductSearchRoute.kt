package ru.ari.caloriescounter.feature.diary.presentation.meal.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.R
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
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is ProductSearchEffect.NavigateToProductDetails ->
                    onNavigateToProductDetails(effect.mealType, effect.product)
                is ProductSearchEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(message = context.getString(effect.messageResId))
                is ProductSearchEffect.ProductQuickAdded ->
                    snackbarHostState.showSnackbar(
                        message = context.getString(
                            R.string.meal_products_search_quick_add_success_with_name,
                            effect.productName,
                        ),
                    )
            }
        }
    }

    ProductSearchScreen(
        state = state.value,
        contentPadding = contentPadding,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onQueryChanged = { viewModel.onIntent(ProductSearchIntent.QueryChanged(it)) },
        onSearchSubmit = { viewModel.onIntent(ProductSearchIntent.SubmitSearch) },
        onProductClick = { product -> viewModel.onIntent(ProductSearchIntent.ProductClicked(product)) },
        onQuickAddClick = { product -> viewModel.onIntent(ProductSearchIntent.QuickAddClicked(product)) },
    )
}
