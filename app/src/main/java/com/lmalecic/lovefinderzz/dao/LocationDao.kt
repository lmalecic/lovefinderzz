package com.lmalecic.lovefinderzz.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.lmalecic.lovefinderzz.entity.LocationDetails
import com.lmalecic.lovefinderzz.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("""
        SELECT * FROM locations
        ORDER BY name COLLATE NOCASE
    """)
    fun observeAll(): Flow<List<LocationEntity>>

    @Transaction
    @Query("""
        SELECT * FROM locations
        WHERE id = :locationId
        LIMIT 1
    """)
    fun observeDetails(locationId: Long): Flow<LocationDetails?>

    @Query("""
        SELECT * FROM locations
        WHERE favorite = 1
        ORDER BY name COLLATE NOCASE
    """)
    fun observeFavorites(): Flow<List<LocationEntity>>

    @Query("""
        UPDATE locations
        SET favorite = :favorite
        WHERE id = :locationId
    """)
    suspend fun setFavorite(locationId: Long, favorite: Boolean)
}