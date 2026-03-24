package ru.ari.caloriescounter.feature.diary.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.datastore.userProfileDataStore
import ru.ari.caloriescounter.feature.diary.domain.UserProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex

@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : UserProfileRepository {

    override fun observeUserProfile(): Flow<UserProfile?> =
        context.userProfileDataStore.data.map { preferences ->
            preferences.toUserProfileOrNull()
        }

    override fun observeOnboardingCompleted(): Flow<Boolean> =
        context.userProfileDataStore.data.map { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] == true
        }

    override suspend fun saveUserProfile(profile: UserProfile) {
        val normalized = profile.normalize()
        context.userProfileDataStore.edit { preferences ->
            preferences[Keys.FIRST_NAME] = normalized.firstName
            preferences[Keys.LAST_NAME] = normalized.lastName
            preferences[Keys.SEX] = normalized.sex.name
            preferences[Keys.AGE_YEARS] = normalized.ageYears
            preferences[Keys.HEIGHT_CM] = normalized.heightCm
            preferences[Keys.ACTIVITY_LEVEL] = normalized.activityLevel.name
            preferences[Keys.GOAL_TYPE] = normalized.goalType.name
        }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        context.userProfileDataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    private fun Preferences.toUserProfileOrNull(): UserProfile? {
        val firstName = this[Keys.FIRST_NAME]?.trim().orEmpty()
        val lastName = this[Keys.LAST_NAME]?.trim().orEmpty()
        val sex = this[Keys.SEX]?.let { runCatching { UserSex.valueOf(it) }.getOrNull() } ?: return null
        val age = this[Keys.AGE_YEARS] ?: return null
        val height = this[Keys.HEIGHT_CM] ?: return null
        val activity =
            this[Keys.ACTIVITY_LEVEL]?.let { runCatching { ActivityLevel.valueOf(it) }.getOrNull() } ?: return null
        val goal = this[Keys.GOAL_TYPE]?.let { runCatching { GoalType.valueOf(it) }.getOrNull() } ?: return null

        return UserProfile(
            firstName = firstName,
            lastName = lastName,
            sex = sex,
            ageYears = age,
            heightCm = height,
            activityLevel = activity,
            goalType = goal,
        )
    }

    private fun UserProfile.normalize(): UserProfile =
        copy(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            ageYears = ageYears.coerceIn(MIN_AGE_YEARS, MAX_AGE_YEARS),
            heightCm = heightCm.coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM),
        )
}

private object Keys {
    val FIRST_NAME = stringPreferencesKey("first_name")
    val LAST_NAME = stringPreferencesKey("last_name")
    val SEX = stringPreferencesKey("sex")
    val AGE_YEARS = intPreferencesKey("age_years")
    val HEIGHT_CM = intPreferencesKey("height_cm")
    val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
    val GOAL_TYPE = stringPreferencesKey("goal_type")
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
}

private const val MIN_AGE_YEARS = 14
private const val MAX_AGE_YEARS = 120
private const val MIN_HEIGHT_CM = 120
private const val MAX_HEIGHT_CM = 250
