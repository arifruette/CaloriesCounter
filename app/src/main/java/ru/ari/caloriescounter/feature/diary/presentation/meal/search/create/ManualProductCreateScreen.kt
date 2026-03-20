package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract.ManualProductCreateState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualProductCreateScreen(
    state: ManualProductCreateState,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged: (String) -> Unit,
    onFatChanged: (String) -> Unit,
    onCarbsChanged: (String) -> Unit,
    onGramsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.meal_products_manual_create_title)) },
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 8.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                OutlinedTextField(
                    value = state.nameInput,
                    onValueChange = onNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_name_label)) },
                    isError = state.nameHasError,
                    supportingText = {
                        if (state.nameHasError) {
                            Text(stringResource(R.string.meal_products_manual_name_error))
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
            }
            item {
                OutlinedTextField(
                    value = state.caloriesInput,
                    onValueChange = onCaloriesChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_calories_label)) },
                    isError = state.caloriesHasError,
                    supportingText = {
                        if (state.caloriesHasError) {
                            Text(stringResource(R.string.meal_products_manual_nutrition_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )
            }
            item {
                OutlinedTextField(
                    value = state.proteinInput,
                    onValueChange = onProteinChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_protein_label)) },
                    isError = state.proteinHasError,
                    supportingText = {
                        if (state.proteinHasError) {
                            Text(stringResource(R.string.meal_products_manual_nutrition_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )
            }
            item {
                OutlinedTextField(
                    value = state.fatInput,
                    onValueChange = onFatChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_fat_label)) },
                    isError = state.fatHasError,
                    supportingText = {
                        if (state.fatHasError) {
                            Text(stringResource(R.string.meal_products_manual_nutrition_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )
            }
            item {
                OutlinedTextField(
                    value = state.carbsInput,
                    onValueChange = onCarbsChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_carbs_label)) },
                    isError = state.carbsHasError,
                    supportingText = {
                        if (state.carbsHasError) {
                            Text(stringResource(R.string.meal_products_manual_nutrition_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )
            }
            item {
                OutlinedTextField(
                    value = state.gramsInput,
                    onValueChange = onGramsChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.meal_products_manual_grams_label)) },
                    isError = state.gramsHasError,
                    supportingText = {
                        if (state.gramsHasError) {
                            Text(stringResource(R.string.meal_products_grams_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSaveClick() }),
                    singleLine = true,
                )
            }
            item {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving,
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(text = stringResource(R.string.meal_products_manual_create_save_action))
                    }
                }
            }
        }
    }
}
