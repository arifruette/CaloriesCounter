package ru.ari.caloriescounter.core.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    entities = [CalorieEntryEntity::class, WeightProfileEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calorieEntryDao(): CalorieEntryDao
    abstract fun weightProfileDao(): WeightProfileDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS weight_profile (
                id INTEGER NOT NULL,
                currentWeightKg REAL NOT NULL,
                targetWeightKg REAL NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent(),
        )
    }
}
