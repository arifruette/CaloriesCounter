package ru.ari.caloriescounter.feature.stats.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.StatsViewModel
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsIntent

@Composable
fun StatsRoute(
    contentPadding: PaddingValues,
    viewModel: StatsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(StatsIntent.ScreenOpened)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                top = contentPadding.calculateTopPadding() + 24.dp,
                end = 24.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.screen_stats),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.route_stats),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
        if (!state.value.isReady) {
            Text(
                text = stringResource(R.string.placeholder_loading),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
