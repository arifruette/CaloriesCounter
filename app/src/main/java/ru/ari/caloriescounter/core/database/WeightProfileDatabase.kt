package ru.ari.caloriescounter.core.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "weight_profile")
data class WeightProfileEntity(
    @PrimaryKey val id: Int = WEIGHT_PROFILE_ID,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
)

@Dao
interface WeightProfileDao {
    @Query("SELECT * FROM weight_profile WHERE id = :id")
    fun observeProfile(id: Int = WEIGHT_PROFILE_ID): Flow<WeightProfileEntity?>

    @Query("SELECT * FROM weight_profile WHERE id = :id")
    suspend fun getProfile(id: Int = WEIGHT_PROFILE_ID): WeightProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: WeightProfileEntity)
}

const val WEIGHT_PROFILE_ID = 1
