package ru.ari.caloriescounter.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calorie_entries")
data class CalorieEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayDate: String,
    val mealType: String,
    val productSource: String,
    val productExternalId: String?,
    val productBarcode: String?,
    val productNameRu: String,
    val productNameOriginal: String?,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val portionGrams: Double,
)
