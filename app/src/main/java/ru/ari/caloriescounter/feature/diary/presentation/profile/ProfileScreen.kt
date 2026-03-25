package ru.ari.caloriescounter.feature.diary.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract.ProfileState
import kotlin.math.abs

@Composable
fun ProfileScreen(
    state: ProfileState,
    contentPadding: PaddingValues,
    onUserProfileClick: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            end = 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        item {
            UserSummaryCard(state = state)
        }
        item {
            WeightProgressCard(state = state)
        }
        item {
            SettingsSection(
                onUserProfileClick = onUserProfileClick,
                state = state,
            )
        }
    }
}

@Composable
private fun UserSummaryCard(state: ProfileState) {
    ProfileCard {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = state.displayName(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(
                    R.string.profile_remaining_calories,
                    state.remainingCalories,
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun WeightProgressCard(state: ProfileState) {
    ProfileCard {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.profile_weight_progress_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WeightMetric(
                    label = stringResource(R.string.profile_weight_start),
                    value = state.initialWeightKg,
                )
                WeightMetric(
                    label = state.weightDeltaLabel(),
                    value = state.currentWeightKg,
                )
                WeightMetric(
                    label = stringResource(R.string.profile_weight_target),
                    value = state.targetWeightKg,
                )
            }
        }
    }
}

@Composable
private fun WeightMetric(
    label: String,
    value: Double,
) {
    androidx.compose.foundation.layout.Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.weight_value, value.formatRuDecimal()),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SettingsSection(
    state: ProfileState,
    onUserProfileClick: () -> Unit,
) {
    MainParametersCard(
        title = stringResource(R.string.profile_hub_main_parameters_title),
        rows = state.parametersPreviewRows(),
        onClick = onUserProfileClick,
    )
}

@Composable
private fun MainParametersCard(
    title: String,
    rows: List<String>,
    onClick: () -> Unit,
) {
    ProfileCard {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            rows.forEach { row ->
                Text(
                    text = row,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = stringResource(R.string.action_edit),
                    modifier = Modifier
                        .clickable(onClick = onClick)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        content()
    }
}


@Composable
private fun ProfileState.displayName(): String {
    val first = firstName.trim()
    val last = lastName.trim()
    if (first.isEmpty() || last.isEmpty()) {
        return stringResource(R.string.profile_user_fallback_name)
    }

    return "$first $last"
}

@Composable
private fun ProfileState.weightDeltaLabel(): String {
    val delta = currentWeightKg - initialWeightKg
    val normalized = if (abs(delta) < 0.0001) 0.0 else delta
    val signedValue = if (normalized > 0.0) {
        "+${normalized.formatRuDecimal()}"
    } else {
        normalized.formatRuDecimal()
    }

    return stringResource(R.string.weight_value, signedValue)
}

@Composable
private fun ProfileState.parametersPreviewRows(): List<String> {
    val sexText = sex?.label()
    if (sexText == null || ageYears <= 0 || heightCm <= 0) {
        return listOf(stringResource(R.string.profile_hub_main_parameters_description))
    }

    return listOf(
        stringResource(R.string.profile_field_sex) + ": $sexText",
        stringResource(R.string.profile_field_age) + ": $ageYears",
        stringResource(R.string.profile_field_height) + ": $heightCm",
        stringResource(R.string.profile_field_activity) + ": ${activityLevel.label()}",
        stringResource(R.string.profile_field_goal) + ": ${goalType.label()}",
    )
}

@Composable
private fun UserSex.label(): String =
    when (this) {
        UserSex.Male -> stringResource(R.string.profile_sex_male)
        UserSex.Female -> stringResource(R.string.profile_sex_female)
    }

@Composable
private fun ActivityLevel.label(): String =
    when (this) {
        ActivityLevel.Sedentary -> stringResource(R.string.profile_activity_sedentary)
        ActivityLevel.Light -> stringResource(R.string.profile_activity_light)
        ActivityLevel.Moderate -> stringResource(R.string.profile_activity_moderate)
        ActivityLevel.High -> stringResource(R.string.profile_activity_high)
        ActivityLevel.VeryHigh -> stringResource(R.string.profile_activity_very_high)
    }

@Composable
private fun GoalType.label(): String =
    when (this) {
        GoalType.Lose -> stringResource(R.string.profile_goal_lose)
        GoalType.Maintain -> stringResource(R.string.profile_goal_maintain)
        GoalType.Gain -> stringResource(R.string.profile_goal_gain)
    }
