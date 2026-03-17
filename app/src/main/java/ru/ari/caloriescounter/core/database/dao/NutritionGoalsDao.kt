package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.NUTRITION_GOALS_ID
import ru.ari.caloriescounter.core.database.entity.NutritionGoalsEntity

@Dao
interface NutritionGoalsDao {
    @Query("SELECT * FROM nutrition_goals WHERE id = :id")
    fun observeGoals(id: Int = NUTRITION_GOALS_ID): Flow<NutritionGoalsEntity?>

    @Query("SELECT * FROM nutrition_goals WHERE id = :id")
    suspend fun getGoals(id: Int = NUTRITION_GOALS_ID): NutritionGoalsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoals(goals: NutritionGoalsEntity)
}
