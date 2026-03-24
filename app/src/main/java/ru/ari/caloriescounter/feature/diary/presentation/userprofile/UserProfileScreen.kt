package ru.ari.caloriescounter.feature.diary.presentation.userprofile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    state: UserProfileState,
    contentPadding: PaddingValues,
    isOnboarding: Boolean,
    onBackClick: (() -> Unit)?,
    onSexSelected: (UserSex) -> Unit,
    onAgeChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
    onCurrentWeightChanged: (String) -> Unit,
    onTargetWeightChanged: (String) -> Unit,
    onActivityLevelSelected: (ActivityLevel) -> Unit,
    onGoalTypeSelected: (GoalType) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (!isOnboarding) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.profile_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        onBackClick?.let {
                            IconButton(onClick = it) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_back),
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 20.dp,
                    end = 20.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            if (isOnboarding) {
                Text(
                    text = stringResource(R.string.onboarding_profile_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProfilePrimaryFields(
                            state = state,
                            onSexSelected = onSexSelected,
                            onAgeChanged = onAgeChanged,
                            onHeightChanged = onHeightChanged,
                            onCurrentWeightChanged = onCurrentWeightChanged,
                            onTargetWeightChanged = onTargetWeightChanged,
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProfileLifestyleFields(
                            state = state,
                            onActivityLevelSelected = onActivityLevelSelected,
                            onGoalTypeSelected = onGoalTypeSelected,
                        )
                    }
                }
            } else {
                ProfilePrimaryFields(
                    state = state,
                    onSexSelected = onSexSelected,
                    onAgeChanged = onAgeChanged,
                    onHeightChanged = onHeightChanged,
                    onCurrentWeightChanged = onCurrentWeightChanged,
                    onTargetWeightChanged = onTargetWeightChanged,
                )
                ProfileLifestyleFields(
                    state = state,
                    onActivityLevelSelected = onActivityLevelSelected,
                    onGoalTypeSelected = onGoalTypeSelected,
                )
            }

            if (state.showValidationErrors) {
                Text(
                    text = stringResource(R.string.profile_validation_error),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                onClick = onSaveClick,
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(
                        if (isOnboarding) {
                            R.string.onboarding_profile_continue
                        } else {
                            R.string.action_save
                        },
                    ),
                )
            }
        }
    }
}

@Composable
private fun ProfilePrimaryFields(
    state: UserProfileState,
    onSexSelected: (UserSex) -> Unit,
    onAgeChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
    onCurrentWeightChanged: (String) -> Unit,
    onTargetWeightChanged: (String) -> Unit,
) {
    SelectionGroup(
        title = stringResource(R.string.profile_field_sex),
        options = UserSex.entries,
        selected = state.sex,
        labelProvider = { it.labelRes() },
        onSelected = onSexSelected,
    )

    OutlinedTextField(
        value = state.ageInput,
        onValueChange = onAgeChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.profile_field_age)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.showValidationErrors && state.ageInput.toIntOrNull()?.let { it in 14..120 } != true,
        singleLine = true,
    )

    OutlinedTextField(
        value = state.heightInput,
        onValueChange = onHeightChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.profile_field_height)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.showValidationErrors && state.heightInput.toIntOrNull()?.let { it in 120..250 } != true,
        singleLine = true,
    )

    OutlinedTextField(
        value = state.currentWeightInput,
        onValueChange = onCurrentWeightChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.profile_field_current_weight)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        isError = state.showValidationErrors &&
            state.currentWeightInput.replace(',', '.').toDoubleOrNull()?.let { it in 30.0..300.0 } != true,
        singleLine = true,
    )

    OutlinedTextField(
        value = state.targetWeightInput,
        onValueChange = onTargetWeightChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.profile_field_target_weight)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        isError = state.showValidationErrors &&
            state.targetWeightInput.replace(',', '.').toDoubleOrNull()?.let { it in 30.0..300.0 } != true,
        singleLine = true,
    )
}

@Composable
private fun ProfileLifestyleFields(
    state: UserProfileState,
    onActivityLevelSelected: (ActivityLevel) -> Unit,
    onGoalTypeSelected: (GoalType) -> Unit,
) {
    SelectionGroup(
        title = stringResource(R.string.profile_field_activity),
        options = ActivityLevel.entries,
        selected = state.activityLevel,
        labelProvider = { it.labelRes() },
        onSelected = onActivityLevelSelected,
    )

    SelectionGroup(
        title = stringResource(R.string.profile_field_goal),
        options = GoalType.entries,
        selected = state.goalType,
        labelProvider = { it.labelRes() },
        onSelected = onGoalTypeSelected,
    )
}

@Composable
private fun <T> SelectionGroup(
    title: String,
    options: List<T>,
    selected: T?,
    labelProvider: (T) -> Int,
    onSelected: (T) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        options.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelected(item) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                RadioButton(selected = selected == item, onClick = { onSelected(item) })
                Text(
                    text = stringResource(labelProvider(item)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

private fun UserSex.labelRes(): Int =
    when (this) {
        UserSex.Male -> R.string.profile_sex_male
        UserSex.Female -> R.string.profile_sex_female
    }

private fun ActivityLevel.labelRes(): Int =
    when (this) {
        ActivityLevel.Sedentary -> R.string.profile_activity_sedentary
        ActivityLevel.Light -> R.string.profile_activity_light
        ActivityLevel.Moderate -> R.string.profile_activity_moderate
        ActivityLevel.High -> R.string.profile_activity_high
        ActivityLevel.VeryHigh -> R.string.profile_activity_very_high
    }

private fun GoalType.labelRes(): Int =
    when (this) {
        GoalType.Lose -> R.string.profile_goal_lose
        GoalType.Maintain -> R.string.profile_goal_maintain
        GoalType.Gain -> R.string.profile_goal_gain
    }
