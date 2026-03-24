package ru.ari.caloriescounter.feature.diary.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.database.dao.WeightProfileDao
import ru.ari.caloriescounter.core.datastore.userProfileDataStore
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.model.weight.WeightProfile
import kotlin.math.round

@Singleton
class WeightProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weightProfileDao: WeightProfileDao,
) : WeightProfileRepository {

    override fun observeWeightProfile(): Flow<WeightProfile> = flow {
        migrateWeightIfNeeded()
        emitAll(
            context.userProfileDataStore.data.map { preferences ->
                preferences.toWeightProfile()
            },
        )
    }

    override suspend fun initializeWeightProfile(
        initialWeightKg: Double,
        currentWeightKg: Double,
        targetWeightKg: Double,
    ) {
        val initial = normalizeWeight(initialWeightKg)
        val current = normalizeWeight(currentWeightKg)
        val target = normalizeWeight(targetWeightKg)

        context.userProfileDataStore.edit { preferences ->
            preferences[WeightKeys.INITIAL_WEIGHT_KG] = initial
            preferences[WeightKeys.CURRENT_WEIGHT_KG] = current
            preferences[WeightKeys.TARGET_WEIGHT_KG] = target
            preferences[WeightKeys.WEIGHT_MIGRATED] = true
        }
    }

    override suspend fun updateInitialWeight(weightKg: Double) {
        migrateWeightIfNeeded()
        val normalized = normalizeWeight(weightKg)

        context.userProfileDataStore.edit { preferences ->
            val current = preferences[WeightKeys.CURRENT_WEIGHT_KG] ?: normalized
            preferences[WeightKeys.INITIAL_WEIGHT_KG] = normalized
            preferences[WeightKeys.CURRENT_WEIGHT_KG] = current
            if (preferences[WeightKeys.TARGET_WEIGHT_KG] == null) {
                preferences[WeightKeys.TARGET_WEIGHT_KG] = defaultWeightProfile.targetWeightKg
            }
            preferences[WeightKeys.WEIGHT_MIGRATED] = true
        }
    }

    override suspend fun updateCurrentWeight(weightKg: Double) {
        migrateWeightIfNeeded()
        val normalized = normalizeWeight(weightKg)

        context.userProfileDataStore.edit { preferences ->
            val initial = preferences[WeightKeys.INITIAL_WEIGHT_KG] ?: normalized
            preferences[WeightKeys.INITIAL_WEIGHT_KG] = initial
            preferences[WeightKeys.CURRENT_WEIGHT_KG] = normalized
            if (preferences[WeightKeys.TARGET_WEIGHT_KG] == null) {
                preferences[WeightKeys.TARGET_WEIGHT_KG] = defaultWeightProfile.targetWeightKg
            }
            preferences[WeightKeys.WEIGHT_MIGRATED] = true
        }
    }

    override suspend fun updateTargetWeight(weightKg: Double) {
        migrateWeightIfNeeded()
        val normalized = normalizeWeight(weightKg)

        context.userProfileDataStore.edit { preferences ->
            val current = preferences[WeightKeys.CURRENT_WEIGHT_KG] ?: defaultWeightProfile.currentWeightKg
            val initial = preferences[WeightKeys.INITIAL_WEIGHT_KG] ?: current
            preferences[WeightKeys.INITIAL_WEIGHT_KG] = initial
            preferences[WeightKeys.CURRENT_WEIGHT_KG] = current
            preferences[WeightKeys.TARGET_WEIGHT_KG] = normalized
            preferences[WeightKeys.WEIGHT_MIGRATED] = true
        }
    }

    private suspend fun migrateWeightIfNeeded() {
        val preferences = context.userProfileDataStore.data.first()
        if (preferences[WeightKeys.WEIGHT_MIGRATED] == true) return

        val roomProfile = weightProfileDao.getProfile()
        val current = preferences[WeightKeys.CURRENT_WEIGHT_KG]
            ?: preferences[WeightKeys.LEGACY_CURRENT_WEIGHT_KG]
            ?: roomProfile?.currentWeightKg
            ?: defaultWeightProfile.currentWeightKg
        val target = preferences[WeightKeys.TARGET_WEIGHT_KG]
            ?: preferences[WeightKeys.LEGACY_TARGET_WEIGHT_KG]
            ?: roomProfile?.targetWeightKg
            ?: defaultWeightProfile.targetWeightKg
        val initial = preferences[WeightKeys.INITIAL_WEIGHT_KG] ?: roomProfile?.currentWeightKg ?: current

        context.userProfileDataStore.edit { mutablePreferences ->
            mutablePreferences[WeightKeys.INITIAL_WEIGHT_KG] = normalizeWeight(initial)
            mutablePreferences[WeightKeys.CURRENT_WEIGHT_KG] = normalizeWeight(current)
            mutablePreferences[WeightKeys.TARGET_WEIGHT_KG] = normalizeWeight(target)
            mutablePreferences[WeightKeys.WEIGHT_MIGRATED] = true
        }
    }

    private fun Preferences.toWeightProfile(): WeightProfile {
        val current = this[WeightKeys.CURRENT_WEIGHT_KG] ?: defaultWeightProfile.currentWeightKg
        val target = this[WeightKeys.TARGET_WEIGHT_KG] ?: defaultWeightProfile.targetWeightKg
        val initial = this[WeightKeys.INITIAL_WEIGHT_KG] ?: current

        return WeightProfile(
            initialWeightKg = initial,
            currentWeightKg = current,
            targetWeightKg = target,
        )
    }

    private fun normalizeWeight(weightKg: Double): Double =
        round(weightKg.coerceAtLeast(0.0) * 10.0) / 10.0

    private companion object {
        val defaultWeightProfile = WeightProfile(
            initialWeightKg = 70.0,
            currentWeightKg = 70.0,
            targetWeightKg = 65.0,
        )
    }
}

private object WeightKeys {
    val INITIAL_WEIGHT_KG = doublePreferencesKey("initial_weight_kg")
    val CURRENT_WEIGHT_KG = doublePreferencesKey("weight_current_kg")
    val TARGET_WEIGHT_KG = doublePreferencesKey("weight_target_kg")
    val LEGACY_CURRENT_WEIGHT_KG = doublePreferencesKey("current_weight_kg")
    val LEGACY_TARGET_WEIGHT_KG = doublePreferencesKey("target_weight_kg")
    val WEIGHT_MIGRATED = booleanPreferencesKey("weight_migrated")
}

