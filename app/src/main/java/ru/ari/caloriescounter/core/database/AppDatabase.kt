package ru.ari.caloriescounter.core.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "calorie_entries")
data class CalorieEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val calories: Int,
)

@Dao
interface CalorieEntryDao {
    @Query("SELECT * FROM calorie_entries ORDER BY id DESC")
    fun observeEntries(): Flow<List<CalorieEntryEntity>>
}

@Database(
    entities = [CalorieEntryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calorieEntryDao(): CalorieEntryDao
}
