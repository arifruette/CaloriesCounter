package ru.ari.caloriescounter.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.WeightProfileDao
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity
import ru.ari.caloriescounter.core.database.entity.WeightProfileEntity

@Database(
    entities = [CalorieEntryEntity::class, WeightProfileEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calorieEntryDao(): CalorieEntryDao
    abstract fun weightProfileDao(): WeightProfileDao
}
