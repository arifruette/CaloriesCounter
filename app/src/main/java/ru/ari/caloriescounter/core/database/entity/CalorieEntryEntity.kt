package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calorie_entries")
data class CalorieEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val calories: Int,
)
