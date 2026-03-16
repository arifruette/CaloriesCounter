package ru.ari.caloriescounter.feature.diary.presentation.common

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WeightAdjustmentButton(
    label: String,
    contentDescription: String,
    onTap: () -> Unit,
    onLongPressStep: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isLongPressActive by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (!isPressed) {
            isLongPressActive = false
        }
    }

    LaunchedEffect(isPressed, isLongPressActive, onLongPressStep) {
        if (!isPressed || !isLongPressActive) return@LaunchedEffect

        while (isLongPressActive) {
            onLongPressStep()
            delay(LONG_PRESS_REPEAT_DELAY_MS)
        }
    }

    Card(
        modifier = modifier
            .clip(ShapeDefaults.Small)
            .combinedClickable(
                interactionSource = interactionSource,
                onClick = onTap,
                onLongClick = { isLongPressActive = true },
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = MaterialTheme.shapes.small,
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .semantics { this.contentDescription = contentDescription },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

private const val LONG_PRESS_REPEAT_DELAY_MS = 120L
