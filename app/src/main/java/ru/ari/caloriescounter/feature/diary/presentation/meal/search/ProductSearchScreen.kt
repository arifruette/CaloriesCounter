package ru.ari.caloriescounter.feature.diary.presentation.meal.search

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ManualProductUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchTab
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.viewmodel.contract.ProductSearchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    state: ProductSearchState,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onProductClick: (ProductSearchItemUiModel) -> Unit,
    onQuickAddClick: (ProductSearchItemUiModel) -> Unit,
    onTabSelected: (ProductSearchTab) -> Unit,
    onCreateManualProductClick: () -> Unit,
    onManualProductClick: (ManualProductUiModel) -> Unit,
    onManualQuickAddClick: (ManualProductUiModel) -> Unit,
    onManualDeleteClick: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.meal_products_search_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.imePadding(),
            ) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(
                        text = data.visuals.message,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        floatingActionButton = {
            if (state.selectedTab == ProductSearchTab.MANUAL) {
                FloatingActionButton(onClick = onCreateManualProductClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.meal_products_manual_create_action),
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 8.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProductSearchTabs(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
            )
            when (state.selectedTab) {
                ProductSearchTab.API -> ApiSearchContent(
                    state = state,
                    onQueryChanged = onQueryChanged,
                    onSearchSubmit = onSearchSubmit,
                    onProductClick = onProductClick,
                    onQuickAddClick = onQuickAddClick,
                )

                ProductSearchTab.MANUAL -> ManualProductsContent(
                    state = state,
                    onManualProductClick = onManualProductClick,
                    onManualQuickAddClick = onManualQuickAddClick,
                    onManualDeleteClick = onManualDeleteClick,
                )
            }
        }
    }
}

@Composable
private fun ProductSearchTabs(
    selectedTab: ProductSearchTab,
    onTabSelected: (ProductSearchTab) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = ProductSearchTab.entries.size,
            ),
            onClick = { onTabSelected(ProductSearchTab.API) },
            selected = selectedTab == ProductSearchTab.API,
            icon = {},
            label = { Text(text = stringResource(R.string.meal_products_search_tab_api)) },
        )
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = ProductSearchTab.entries.size,
            ),
            onClick = { onTabSelected(ProductSearchTab.MANUAL) },
            selected = selectedTab == ProductSearchTab.MANUAL,
            icon = {},
            label = { Text(text = stringResource(R.string.meal_products_search_tab_manual)) },
        )
    }
}

@Composable
private fun ApiSearchContent(
    state: ProductSearchState,
    onQueryChanged: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onProductClick: (ProductSearchItemUiModel) -> Unit,
    onQuickAddClick: (ProductSearchItemUiModel) -> Unit,
) {
    OutlinedTextField(
        value = state.query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.meal_products_search_hint)) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearchSubmit) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.meal_products_search_action),
                )
            }
        },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(onSearch = { onSearchSubmit() }),
    )

    when {
        state.isLoading -> ProductSearchShimmerList()
        state.hasError -> Text(
            text = stringResource(
                id = state.errorMessageResId ?: R.string.meal_products_search_error,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )

        state.query.isBlank() -> Text(
            text = stringResource(R.string.meal_products_search_initial),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        state.results.isEmpty() -> Text(
            text = stringResource(R.string.meal_products_search_empty),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.results, key = { "${it.source.name}:${it.externalId}:${it.nameRu}" }) { product ->
                    ProductSearchResultCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        onQuickAddClick = { onQuickAddClick(product) },
                        isQuickAddInProgress = state.quickAddInProgressKey == product.stableKey(),
                        quickAddHintRes = R.string.meal_products_search_quick_add_hint,
                    )
                }
            }
        }
    }
}

@Composable
private fun ManualProductsContent(
    state: ProductSearchState,
    onManualProductClick: (ManualProductUiModel) -> Unit,
    onManualQuickAddClick: (ManualProductUiModel) -> Unit,
    onManualDeleteClick: (Long) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.manualProducts.isEmpty()) {
            Text(
                text = stringResource(R.string.meal_products_manual_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.manualProducts, key = { it.id }) { product ->
                    ManualProductCard(
                        product = product,
                        onClick = { onManualProductClick(product) },
                        onQuickAddClick = { onManualQuickAddClick(product) },
                        onDeleteClick = { onManualDeleteClick(product.id) },
                        isQuickAddInProgress = state.manualQuickAddInProgressId == product.id,
                        isDeleteInProgress = state.manualDeleteInProgressId == product.id,
                    )
                }
            }
        }
    }
}

@Composable
private fun ManualProductCard(
    product: ManualProductUiModel,
    onClick: () -> Unit,
    onQuickAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isQuickAddInProgress: Boolean,
    isDeleteInProgress: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = product.nameRu,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(
                        R.string.meal_products_search_result_summary,
                        product.caloriesPer100g.toInt(),
                        product.proteinPer100g.formatRuDecimal(),
                        product.fatPer100g.formatRuDecimal(),
                        product.carbsPer100g.formatRuDecimal(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(
                onClick = onQuickAddClick,
                enabled = !isQuickAddInProgress && !isDeleteInProgress,
            ) {
                if (isQuickAddInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.meal_products_search_quick_add_cd),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            IconButton(
                onClick = onDeleteClick,
                enabled = !isQuickAddInProgress && !isDeleteInProgress,
            ) {
                if (isDeleteInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.meal_products_manual_delete_cd),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductSearchShimmerList() {
    val shimmerBrush = subtleShimmerBrush()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(12) {
            ProductSearchShimmerCard(shimmerBrush = shimmerBrush)
        }
    }
}

@Composable
private fun subtleShimmerBrush(): Brush {
    val baseColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val highlightColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f)
    val transition = rememberInfiniteTransition(label = "search_shimmer_transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 700f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "search_shimmer_translate",
    )

    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(x = translateAnim - 280f, y = 0f),
        end = Offset(x = translateAnim, y = 260f),
    )
}

@Composable
private fun ProductSearchShimmerCard(shimmerBrush: Brush) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerBrush),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerBrush),
            )
        }
    }
}

@Composable
private fun ProductSearchResultCard(
    product: ProductSearchItemUiModel,
    onClick: () -> Unit,
    onQuickAddClick: () -> Unit,
    isQuickAddInProgress: Boolean,
    quickAddHintRes: Int,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = product.nameRu,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(
                        R.string.meal_products_search_result_summary,
                        product.caloriesPer100g.toInt(),
                        product.proteinPer100g.formatRuDecimal(),
                        product.fatPer100g.formatRuDecimal(),
                        product.carbsPer100g.formatRuDecimal(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(quickAddHintRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(
                onClick = onQuickAddClick,
                enabled = !isQuickAddInProgress,
            ) {
                if (isQuickAddInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.meal_products_search_quick_add_cd),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

private fun ProductSearchItemUiModel.stableKey(): String = "${source.name}:${externalId}:${nameRu}"
