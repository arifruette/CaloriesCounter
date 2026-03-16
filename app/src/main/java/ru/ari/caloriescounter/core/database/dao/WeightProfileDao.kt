package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.WEIGHT_PROFILE_ID
import ru.ari.caloriescounter.core.database.entity.WeightProfileEntity

@Dao
interface WeightProfileDao {
    @Query("SELECT * FROM weight_profile WHERE id = :id")
    fun observeProfile(id: Int = WEIGHT_PROFILE_ID): Flow<WeightProfileEntity?>

    @Query("SELECT * FROM weight_profile WHERE id = :id")
    suspend fun getProfile(id: Int = WEIGHT_PROFILE_ID): WeightProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: WeightProfileEntity)
}
