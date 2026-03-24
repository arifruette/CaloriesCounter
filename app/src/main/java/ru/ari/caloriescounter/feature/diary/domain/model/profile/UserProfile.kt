package ru.ari.caloriescounter.feature.diary.domain.model.profile

data class UserProfile(
    val sex: UserSex,
    val ageYears: Int,
    val heightCm: Int,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
)

