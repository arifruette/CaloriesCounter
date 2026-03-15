package ru.ari.caloriescounter.feature.diary.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.database.WeightProfileDao
import ru.ari.caloriescounter.core.database.WeightProfileEntity
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.model.WeightProfile
import kotlin.math.round

@Singleton
class WeightProfileRepositoryImpl @Inject constructor(
    private val weightProfileDao: WeightProfileDao,
) : WeightProfileRepository {

    override fun observeWeightProfile(): Flow<WeightProfile> =
        weightProfileDao.observeProfile().map { entity ->
            entity?.toDomain() ?: defaultWeightProfile
        }

    override suspend fun updateCurrentWeight(weightKg: Double) {
        val current = getStoredOrDefault()
        weightProfileDao.upsertProfile(
            current.copy(currentWeightKg = normalizeWeight(weightKg)),
        )
    }

    override suspend fun updateTargetWeight(weightKg: Double) {
        val current = getStoredOrDefault()
        weightProfileDao.upsertProfile(
            current.copy(targetWeightKg = normalizeWeight(weightKg)),
        )
    }

    private suspend fun getStoredOrDefault(): WeightProfileEntity =
        weightProfileDao.getProfile() ?: defaultWeightProfile.toEntity()

    private fun normalizeWeight(weightKg: Double): Double =
        round(weightKg.coerceAtLeast(0.0) * 10.0) / 10.0

    private fun WeightProfileEntity.toDomain(): WeightProfile =
        WeightProfile(
            currentWeightKg = currentWeightKg,
            targetWeightKg = targetWeightKg,
        )

    private fun WeightProfile.toEntity(): WeightProfileEntity =
        WeightProfileEntity(
            currentWeightKg = currentWeightKg,
            targetWeightKg = targetWeightKg,
        )

    private companion object {
        val defaultWeightProfile = WeightProfile(
            currentWeightKg = 70.0,
            targetWeightKg = 65.0,
        )
    }
}
