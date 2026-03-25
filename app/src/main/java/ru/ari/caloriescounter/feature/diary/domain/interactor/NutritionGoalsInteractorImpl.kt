package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.diary.domain.NutritionGoalsRepository
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionRecommendation
import ru.ari.caloriescounter.feature.diary.domain.model.profile.ActivityLevel
import ru.ari.caloriescounter.feature.diary.domain.model.profile.GoalType
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserProfile
import ru.ari.caloriescounter.feature.diary.domain.model.profile.UserSex
import kotlin.math.roundToInt

class NutritionGoalsInteractorImpl @Inject constructor(
    private val repository: NutritionGoalsRepository,
) : NutritionGoalsInteractor {
    override fun observeGoals(): Flow<NutritionGoals> = repository.observeGoals()

    override fun calculateRecommendation(profile: UserProfile, currentWeightKg: Double): NutritionRecommendation {
        val normalizedWeightKg = currentWeightKg.coerceIn(MIN_WEIGHT_KG, MAX_WEIGHT_KG)
        val bmr = when (profile.sex) {
            UserSex.Male -> 10.0 * normalizedWeightKg + 6.25 * profile.heightCm - 5.0 * profile.ageYears + 5.0
            UserSex.Female -> 10.0 * normalizedWeightKg + 6.25 * profile.heightCm - 5.0 * profile.ageYears - 161.0
        }
        val activityMultiplier = profile.activityLevel.multiplier()
        val tdee = bmr * activityMultiplier
        val rawTargetCalories = tdee * profile.goalType.calorieAdjustmentMultiplier()
        val targetCalories = rawTargetCalories.roundToInt().coerceIn(
            minimumCalories(profile.sex),
            MAX_CALORIES,
        )
        val macroSplit = profile.goalType.macroSplit()
        val protein = targetCalories * macroSplit.proteinShare / KCAL_PER_GRAM_PROTEIN
        val fat = targetCalories * macroSplit.fatShare / KCAL_PER_GRAM_FAT
        val carbsCalories = targetCalories * macroSplit.carbsShare
        val carbs = carbsCalories / KCAL_PER_GRAM_CARBS

        return NutritionRecommendation(
            goals = NutritionGoals(
                calories = targetCalories,
                protein = (protein * 10.0).roundToInt() / 10.0,
                fat = (fat * 10.0).roundToInt() / 10.0,
                carbs = (carbs * 10.0).roundToInt() / 10.0,
            ),
            bmr = bmr,
            tdee = tdee,
            activityMultiplier = activityMultiplier,
            targetCaloriesBeforeClamping = rawTargetCalories,
        )
    }

    override suspend fun updateGoals(goals: NutritionGoals) {
        repository.updateGoals(goals)
    }
}

private data class MacroSplit(
    val proteinShare: Double,
    val fatShare: Double,
    val carbsShare: Double,
)

private fun ActivityLevel.multiplier(): Double =
    when (this) {
        ActivityLevel.Sedentary -> 1.2
        ActivityLevel.Light -> 1.375
        ActivityLevel.Moderate -> 1.55
        ActivityLevel.High -> 1.725
        ActivityLevel.VeryHigh -> 1.9
    }

private fun GoalType.calorieAdjustmentMultiplier(): Double =
    when (this) {
        GoalType.Lose -> 0.9
        GoalType.Maintain -> 1.0
        GoalType.Gain -> 1.1
    }

private fun GoalType.macroSplit(): MacroSplit =
    when (this) {
        GoalType.Lose -> MacroSplit(proteinShare = 0.35, fatShare = 0.30, carbsShare = 0.35)
        GoalType.Maintain -> MacroSplit(proteinShare = 0.30, fatShare = 0.30, carbsShare = 0.40)
        GoalType.Gain -> MacroSplit(proteinShare = 0.25, fatShare = 0.25, carbsShare = 0.50)
    }

private fun minimumCalories(sex: UserSex): Int =
    when (sex) {
        UserSex.Male -> 1500
        UserSex.Female -> 1200
    }

private const val MIN_WEIGHT_KG = 30.0
private const val MAX_WEIGHT_KG = 300.0
private const val MAX_CALORIES = 4500
private const val KCAL_PER_GRAM_PROTEIN = 4.0
private const val KCAL_PER_GRAM_FAT = 9.0
private const val KCAL_PER_GRAM_CARBS = 4.0
