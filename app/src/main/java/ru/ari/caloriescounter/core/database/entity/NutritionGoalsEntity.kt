package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ari.caloriescounter.core.database.NUTRITION_GOALS_ID

@Entity(tableName = "nutrition_goals")
data class NutritionGoalsEntity(
    @PrimaryKey val id: Int = NUTRITION_GOALS_ID,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
)
