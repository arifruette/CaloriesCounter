package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity

@Dao
interface CalorieEntryDao {
    @Query("SELECT * FROM calorie_entries ORDER BY id DESC")
    fun observeEntries(): Flow<List<CalorieEntryEntity>>
}
