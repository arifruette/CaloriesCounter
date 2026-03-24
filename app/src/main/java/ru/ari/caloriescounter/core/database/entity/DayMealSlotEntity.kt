package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "day_meal_slots",
    indices = [
        Index(value = ["dayDate", "mealKey"], unique = true),
        Index(value = ["dayDate", "sortOrder"]),
    ],
)
data class DayMealSlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayDate: String,
    val mealKey: String,
    val title: String,
    val sortOrder: Int,
    val isBase: Boolean,
)

