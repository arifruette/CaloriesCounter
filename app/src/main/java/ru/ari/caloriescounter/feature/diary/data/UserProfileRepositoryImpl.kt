package ru.ari.caloriescounter.feature.diary.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
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
import kotlin.math.round

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
            preferences[Keys.ONBOARDING_COMPLETED] == true && preferences.toUserProfileOrNull() != null
        }

    override suspend fun saveUserProfile(profile: UserProfile) {
        val normalized = profile.normalize()
        context.userProfileDataStore.edit { preferences ->
            preferences[Keys.SEX] = normalized.sex.name
            preferences[Keys.AGE_YEARS] = normalized.ageYears
            preferences[Keys.HEIGHT_CM] = normalized.heightCm
            preferences[Keys.CURRENT_WEIGHT_KG] = normalized.currentWeightKg
            preferences[Keys.TARGET_WEIGHT_KG] = normalized.targetWeightKg
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
        val sex = this[Keys.SEX]?.let { runCatching { UserSex.valueOf(it) }.getOrNull() } ?: return null
        val age = this[Keys.AGE_YEARS] ?: return null
        val height = this[Keys.HEIGHT_CM] ?: return null
        val currentWeight = this[Keys.CURRENT_WEIGHT_KG] ?: return null
        val targetWeight = this[Keys.TARGET_WEIGHT_KG] ?: return null
        val activity =
            this[Keys.ACTIVITY_LEVEL]?.let { runCatching { ActivityLevel.valueOf(it) }.getOrNull() } ?: return null
        val goal = this[Keys.GOAL_TYPE]?.let { runCatching { GoalType.valueOf(it) }.getOrNull() } ?: return null

        return UserProfile(
            sex = sex,
            ageYears = age,
            heightCm = height,
            currentWeightKg = currentWeight,
            targetWeightKg = targetWeight,
            activityLevel = activity,
            goalType = goal,
        )
    }

    private fun UserProfile.normalize(): UserProfile =
        copy(
            ageYears = ageYears.coerceIn(MIN_AGE_YEARS, MAX_AGE_YEARS),
            heightCm = heightCm.coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM),
            currentWeightKg = round(currentWeightKg.coerceIn(MIN_WEIGHT_KG, MAX_WEIGHT_KG) * 10.0) / 10.0,
            targetWeightKg = round(targetWeightKg.coerceIn(MIN_WEIGHT_KG, MAX_WEIGHT_KG) * 10.0) / 10.0,
        )
}

private object Keys {
    val SEX = stringPreferencesKey("sex")
    val AGE_YEARS = intPreferencesKey("age_years")
    val HEIGHT_CM = intPreferencesKey("height_cm")
    val CURRENT_WEIGHT_KG = doublePreferencesKey("current_weight_kg")
    val TARGET_WEIGHT_KG = doublePreferencesKey("target_weight_kg")
    val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
    val GOAL_TYPE = stringPreferencesKey("goal_type")
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
}

private const val MIN_AGE_YEARS = 14
private const val MAX_AGE_YEARS = 120
private const val MIN_HEIGHT_CM = 120
private const val MAX_HEIGHT_CM = 250
private const val MIN_WEIGHT_KG = 30.0
private const val MAX_WEIGHT_KG = 300.0

