package ru.ari.caloriescounter.feature.diary.domain.model.profile

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val sex: UserSex,
    val ageYears: Int,
    val heightCm: Int,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
)
