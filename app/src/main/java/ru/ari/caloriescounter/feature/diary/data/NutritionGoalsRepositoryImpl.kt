package ru.ari.caloriescounter.feature.diary.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ari.caloriescounter.core.database.dao.NutritionGoalsDao
import ru.ari.caloriescounter.core.database.entity.NutritionGoalsEntity
import ru.ari.caloriescounter.feature.diary.domain.NutritionGoalsRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import kotlin.math.round

@Singleton
class NutritionGoalsRepositoryImpl @Inject constructor(
    private val goalsDao: NutritionGoalsDao,
) : NutritionGoalsRepository {

    override fun observeGoals(): Flow<NutritionGoals> =
        goalsDao.observeGoals().map { entity ->
            entity?.toDomain() ?: defaultGoals
        }

    override suspend fun updateGoals(goals: NutritionGoals) {
        goalsDao.upsertGoals(goals.normalize().toEntity())
    }

    private fun NutritionGoalsEntity.toDomain(): NutritionGoals =
        NutritionGoals(
            calories = calories,
            protein = protein,
            fat = fat,
            carbs = carbs,
        )

    private fun NutritionGoals.toEntity(): NutritionGoalsEntity =
        NutritionGoalsEntity(
            calories = calories,
            protein = protein,
            fat = fat,
            carbs = carbs,
        )

    private fun NutritionGoals.normalize(): NutritionGoals =
        NutritionGoals(
            calories = calories.coerceAtLeast(1),
            protein = round(protein.coerceAtLeast(0.1) * 10.0) / 10.0,
            fat = round(fat.coerceAtLeast(0.1) * 10.0) / 10.0,
            carbs = round(carbs.coerceAtLeast(0.1) * 10.0) / 10.0,
        )

    private companion object {
        val defaultGoals = NutritionGoals(
            calories = 2000,
            protein = 100.0,
            fat = 70.0,
            carbs = 250.0,
        )
    }
}
