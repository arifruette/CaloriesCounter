package ru.ari.caloriescounter.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.core.database.entity.ManualProductEntity

@Dao
interface ManualProductDao {
    @Query("SELECT * FROM manual_products ORDER BY createdAtEpochMillis DESC, id DESC")
    fun observeAll(): Flow<List<ManualProductEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(product: ManualProductEntity): Long

    @Query("DELETE FROM manual_products WHERE id = :productId")
    suspend fun deleteById(productId: Long)
}
