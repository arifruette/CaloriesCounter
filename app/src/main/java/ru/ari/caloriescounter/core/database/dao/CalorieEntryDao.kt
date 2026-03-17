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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: CalorieEntryEntity)

    @Query("DELETE FROM calorie_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Long)
}
