package ru.ari.caloriescounter.feature.diary.presentation.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.ProfileViewModel
import ru.ari.caloriescounter.feature.diary.presentation.profile.viewmodel.contract.ProfileIntent

@Composable
fun ProfileRoute(
    contentPadding: PaddingValues,
    onNavigateToUserProfile: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(ProfileIntent.ScreenOpened)
    }

    ProfileScreen(
        state = state.value,
        contentPadding = contentPadding,
        onUserProfileClick = onNavigateToUserProfile,
    )
}

