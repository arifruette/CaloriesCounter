package ru.ari.caloriescounter.feature.diary.presentation.userprofile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.UserProfileViewModel
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileEffect
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileIntent

@Composable
fun UserProfileRoute(
    contentPadding: PaddingValues,
    isOnboarding: Boolean,
    onBackClick: (() -> Unit)?,
    onSaved: () -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(UserProfileIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                UserProfileEffect.Saved -> onSaved()
            }
        }
    }

    UserProfileScreen(
        state = state.value,
        contentPadding = contentPadding,
        isOnboarding = isOnboarding,
        onBackClick = onBackClick,
        onSexSelected = { viewModel.onIntent(UserProfileIntent.SexSelected(it)) },
        onAgeChanged = { viewModel.onIntent(UserProfileIntent.AgeChanged(it)) },
        onHeightChanged = { viewModel.onIntent(UserProfileIntent.HeightChanged(it)) },
        onCurrentWeightChanged = { viewModel.onIntent(UserProfileIntent.CurrentWeightChanged(it)) },
        onTargetWeightChanged = { viewModel.onIntent(UserProfileIntent.TargetWeightChanged(it)) },
        onActivityLevelSelected = { viewModel.onIntent(UserProfileIntent.ActivityLevelSelected(it)) },
        onGoalTypeSelected = { viewModel.onIntent(UserProfileIntent.GoalTypeSelected(it)) },
        onSaveClick = { viewModel.onIntent(UserProfileIntent.SaveClicked) },
    )
}

