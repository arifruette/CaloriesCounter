package ru.ari.caloriescounter.feature.diary.domain

import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile

interface UserProfileRepository {
    fun observeUserProfile(): Flow<UserProfile?>
    fun observeOnboardingCompleted(): Flow<Boolean>
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun setOnboardingCompleted(completed: Boolean)
}

