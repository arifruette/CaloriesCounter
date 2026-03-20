package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "manual_products",
    indices = [Index(value = ["createdAtEpochMillis"])],
)
data class ManualProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameRu: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val createdAtEpochMillis: Long,
)
