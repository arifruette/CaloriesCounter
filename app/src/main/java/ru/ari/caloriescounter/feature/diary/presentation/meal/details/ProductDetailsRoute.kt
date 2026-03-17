package ru.ari.caloriescounter.feature.diary.presentation.meal.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.ProductDetailsViewModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract.ProductDetailsEffect
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract.ProductDetailsIntent

@Composable
fun ProductDetailsRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onProductAdded: () -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                ProductDetailsEffect.ProductAdded -> onProductAdded()
            }
        }
    }

    ProductDetailsScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onGramsChanged = { value -> viewModel.onIntent(ProductDetailsIntent.GramsChanged(value)) },
        onAddClick = { viewModel.onIntent(ProductDetailsIntent.AddClicked) },
    )
}
