package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.entity.DayMealSlotEntity

@Dao
interface DayMealSlotDao {
    @Query("SELECT * FROM day_meal_slots WHERE dayDate = :dayDate ORDER BY sortOrder ASC, id ASC")
    fun observeSlotsByDate(dayDate: String): Flow<List<DayMealSlotEntity>>

    @Query("SELECT * FROM day_meal_slots WHERE dayDate = :dayDate ORDER BY sortOrder ASC, id ASC")
    suspend fun getSlotsByDate(dayDate: String): List<DayMealSlotEntity>

    @Query("SELECT * FROM day_meal_slots WHERE dayDate = :dayDate AND mealKey = :mealKey LIMIT 1")
    suspend fun getSlotByDateAndKey(dayDate: String, mealKey: String): DayMealSlotEntity?

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM day_meal_slots WHERE dayDate = :dayDate")
    suspend fun maxSortOrder(dayDate: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlots(slots: List<DayMealSlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlot(slot: DayMealSlotEntity): Long

    @Query(
        """
        UPDATE day_meal_slots
        SET title = :title
        WHERE dayDate = :dayDate AND mealKey = :mealKey
        """,
    )
    suspend fun updateSlotTitle(dayDate: String, mealKey: String, title: String)

    @Query("DELETE FROM day_meal_slots WHERE dayDate = :dayDate AND mealKey = :mealKey")
    suspend fun deleteSlot(dayDate: String, mealKey: String)
}

