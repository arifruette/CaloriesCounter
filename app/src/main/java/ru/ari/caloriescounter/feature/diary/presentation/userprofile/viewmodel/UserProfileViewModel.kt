package ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.diary.domain.interactor.UserProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileEffect
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileIntent
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.viewmodel.contract.UserProfileState

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileInteractor: UserProfileInteractor,
    private val weightProfileInteractor: WeightProfileInteractor,
) : BaseMviViewModel<UserProfileIntent, UserProfileState, UserProfileEffect>(UserProfileState()) {

    private var hasLoaded = false

    override fun onIntent(intent: UserProfileIntent) {
        when (intent) {
            UserProfileIntent.ScreenOpened -> loadInitialData()
            is UserProfileIntent.FirstNameChanged -> updateState {
                copy(firstNameInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.LastNameChanged -> updateState {
                copy(lastNameInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.SexSelected -> updateState {
                copy(sex = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.AgeChanged -> updateState {
                copy(ageInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.HeightChanged -> updateState {
                copy(heightInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.InitialWeightChanged -> updateState {
                copy(initialWeightInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.CurrentWeightChanged -> updateState {
                copy(currentWeightInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.TargetWeightChanged -> updateState {
                copy(targetWeightInput = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.ActivityLevelSelected -> updateState {
                copy(activityLevel = intent.value, showValidationErrors = false)
            }
            is UserProfileIntent.GoalTypeSelected -> updateState {
                copy(goalType = intent.value, showValidationErrors = false)
            }
            UserProfileIntent.SaveClicked -> saveProfile()
        }
    }

    private fun loadInitialData() {
        if (hasLoaded) return
        hasLoaded = true

        viewModelScope.launch {
            val storedProfile = userProfileInteractor.observeUserProfile().first()
            val weightProfile = weightProfileInteractor.observeWeightProfile().first()
            val state = if (storedProfile != null) {
                storedProfile.toState(
                    initialWeight = weightProfile.initialWeightKg,
                    currentWeight = weightProfile.currentWeightKg,
                    targetWeight = weightProfile.targetWeightKg,
                )
            } else {
                UserProfileState(
                    firstNameInput = "",
                    lastNameInput = "",
                    sex = null,
                    ageInput = "",
                    heightInput = "",
                    initialWeightInput = weightProfile.initialWeightKg.formatInput(),
                    currentWeightInput = weightProfile.currentWeightKg.formatInput(),
                    targetWeightInput = weightProfile.targetWeightKg.formatInput(),
                    isLoading = false,
                )
            }

            updateState { state }
        }
    }

    private fun saveProfile() {
        val profile = state.value.toUserProfileOrNull()
        val initialWeight = state.value.initialWeightInput.parseWeight()
            ?.takeIf { it in MIN_WEIGHT_KG..MAX_WEIGHT_KG }
        val currentWeight = state.value.currentWeightInput.parseWeight()
            ?.takeIf { it in MIN_WEIGHT_KG..MAX_WEIGHT_KG }
        val targetWeight = state.value.targetWeightInput.parseWeight()
            ?.takeIf { it in MIN_WEIGHT_KG..MAX_WEIGHT_KG }

        if (profile == null || initialWeight == null || currentWeight == null || targetWeight == null) {
            updateState { copy(showValidationErrors = true) }
            return
        }

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            val onboardingCompleted = userProfileInteractor.observeOnboardingCompleted().first()
            userProfileInteractor.saveUserProfile(profile)
            if (!onboardingCompleted) {
                weightProfileInteractor.initializeWeightProfile(
                    initialWeightKg = initialWeight,
                    currentWeightKg = currentWeight,
                    targetWeightKg = targetWeight,
                )
            } else {
                weightProfileInteractor.updateInitialWeight(initialWeight)
                weightProfileInteractor.updateCurrentWeight(currentWeight)
                weightProfileInteractor.updateTargetWeight(targetWeight)
            }
            userProfileInteractor.setOnboardingCompleted(true)
            updateState { copy(isSaving = false, showValidationErrors = false) }
            emitEffect(UserProfileEffect.Saved)
        }
    }
}

private fun UserProfile.toState(
    initialWeight: Double,
    currentWeight: Double,
    targetWeight: Double,
): UserProfileState =
    UserProfileState(
        isLoading = false,
        firstNameInput = firstName,
        lastNameInput = lastName,
        sex = sex,
        ageInput = ageYears.toString(),
        heightInput = heightCm.toString(),
        initialWeightInput = initialWeight.formatInput(),
        currentWeightInput = currentWeight.formatInput(),
        targetWeightInput = targetWeight.formatInput(),
        activityLevel = activityLevel,
        goalType = goalType,
    )

private fun UserProfileState.toUserProfileOrNull(): UserProfile? {
    val firstNameValue = firstNameInput.trim().takeIf { it.isNotEmpty() } ?: return null
    val lastNameValue = lastNameInput.trim().takeIf { it.isNotEmpty() } ?: return null
    val sexValue = sex ?: return null
    val age = ageInput.toIntOrNull()?.takeIf { it in MIN_AGE_YEARS..MAX_AGE_YEARS } ?: return null
    val height = heightInput.toIntOrNull()?.takeIf { it in MIN_HEIGHT_CM..MAX_HEIGHT_CM } ?: return null

    return UserProfile(
        firstName = firstNameValue,
        lastName = lastNameValue,
        sex = sexValue,
        ageYears = age,
        heightCm = height,
        activityLevel = activityLevel,
        goalType = goalType,
    )
}

private fun Double.formatInput(): String {
    val intValue = toInt()
    return if (this == intValue.toDouble()) intValue.toString() else toString()
}

private fun String.parseWeight(): Double? = replace(',', '.').toDoubleOrNull()

private const val MIN_AGE_YEARS = 14
private const val MAX_AGE_YEARS = 120
private const val MIN_HEIGHT_CM = 120
private const val MAX_HEIGHT_CM = 250
private const val MIN_WEIGHT_KG = 30.0
private const val MAX_WEIGHT_KG = 300.0
