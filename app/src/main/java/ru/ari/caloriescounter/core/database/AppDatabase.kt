package ru.ari.caloriescounter.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.DayMealSlotDao
import ru.ari.caloriescounter.core.database.dao.ManualProductDao
import ru.ari.caloriescounter.core.database.dao.NutritionGoalsDao
import ru.ari.caloriescounter.core.database.dao.WeightProfileDao
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity
import ru.ari.caloriescounter.core.database.entity.DayMealSlotEntity
import ru.ari.caloriescounter.core.database.entity.ManualProductEntity
import ru.ari.caloriescounter.core.database.entity.NutritionGoalsEntity
import ru.ari.caloriescounter.core.database.entity.WeightProfileEntity

@Database(
    entities = [
        CalorieEntryEntity::class,
        WeightProfileEntity::class,
        NutritionGoalsEntity::class,
        ManualProductEntity::class,
        DayMealSlotEntity::class,
    ],
    version = 6,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calorieEntryDao(): CalorieEntryDao
    abstract fun weightProfileDao(): WeightProfileDao
    abstract fun nutritionGoalsDao(): NutritionGoalsDao
    abstract fun manualProductDao(): ManualProductDao
    abstract fun dayMealSlotDao(): DayMealSlotDao
}
