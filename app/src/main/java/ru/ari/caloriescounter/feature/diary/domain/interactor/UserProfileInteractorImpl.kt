package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.UserProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile

class UserProfileInteractorImpl @Inject constructor(
    private val repository: UserProfileRepository,
) : UserProfileInteractor {

    override fun observeUserProfile(): Flow<UserProfile?> = repository.observeUserProfile()

    override fun observeOnboardingCompleted(): Flow<Boolean> = repository.observeOnboardingCompleted()

    override suspend fun saveUserProfile(profile: UserProfile) {
        repository.saveUserProfile(profile)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        repository.setOnboardingCompleted(completed)
    }
}

