package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ari.caloriescounter.core.database.WEIGHT_PROFILE_ID

@Entity(tableName = "weight_profile")
data class WeightProfileEntity(
    @PrimaryKey val id: Int = WEIGHT_PROFILE_ID,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
)
