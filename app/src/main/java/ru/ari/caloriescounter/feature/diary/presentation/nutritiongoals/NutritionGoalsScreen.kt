package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionGoalsScreen(
    state: NutritionGoalsState,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged: (String) -> Unit,
    onFatChanged: (String) -> Unit,
    onCarbsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_nutrition_goals)) },
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
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 24.dp,
                        end = 24.dp,
                        bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 24.dp,
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 16.dp,
                    end = 24.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.caloriesInput,
                onValueChange = onCaloriesChanged,
                label = { Text(stringResource(R.string.meal_products_metric_calories)) },
                isError = state.caloriesHasError,
                supportingText = {
                    if (state.caloriesHasError) {
                        Text(text = stringResource(R.string.nutrition_goals_error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.proteinInput,
                onValueChange = onProteinChanged,
                label = { Text(stringResource(R.string.meal_products_metric_protein)) },
                isError = state.proteinHasError,
                supportingText = {
                    if (state.proteinHasError) {
                        Text(text = stringResource(R.string.nutrition_goals_error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.fatInput,
                onValueChange = onFatChanged,
                label = { Text(stringResource(R.string.meal_products_metric_fat)) },
                isError = state.fatHasError,
                supportingText = {
                    if (state.fatHasError) {
                        Text(text = stringResource(R.string.nutrition_goals_error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.carbsInput,
                onValueChange = onCarbsChanged,
                label = { Text(stringResource(R.string.meal_products_metric_carbs)) },
                isError = state.carbsHasError,
                supportingText = {
                    if (state.carbsHasError) {
                        Text(text = stringResource(R.string.nutrition_goals_error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.nutrition_goals_save))
            }
        }
    }
}
