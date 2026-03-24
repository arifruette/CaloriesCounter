package ru.ari.caloriescounter.feature.diary.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile

interface UserProfileInteractor {
    fun observeUserProfile(): Flow<UserProfile?>
    fun observeOnboardingCompleted(): Flow<Boolean>
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun setOnboardingCompleted(completed: Boolean)
}

