package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity

@Dao
interface CalorieEntryDao {
    @Query("SELECT * FROM calorie_entries WHERE dayDate = :dayDate ORDER BY id DESC")
    fun observeEntriesByDate(dayDate: String): Flow<List<CalorieEntryEntity>>

    @Query(
        "SELECT * FROM calorie_entries " +
            "WHERE dayDate = :dayDate AND mealType = :mealType " +
            "ORDER BY id DESC",
    )
    fun observeEntriesByDateAndMeal(dayDate: String, mealType: String): Flow<List<CalorieEntryEntity>>

    @Query(
        "SELECT * FROM calorie_entries " +
            "WHERE dayDate BETWEEN :startDate AND :endDate " +
            "ORDER BY dayDate ASC, id ASC",
    )
    fun observeEntriesBetween(startDate: String, endDate: String): Flow<List<CalorieEntryEntity>>

    @Query("SELECT DISTINCT dayDate FROM calorie_entries ORDER BY dayDate ASC")
    fun observeLoggedDatesAsc(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: CalorieEntryEntity)

    @Query("DELETE FROM calorie_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Long)
}
