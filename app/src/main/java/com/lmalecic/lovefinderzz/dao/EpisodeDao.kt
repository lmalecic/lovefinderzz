package com.lmalecic.lovefinderzz.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.lmalecic.lovefinderzz.entity.EpisodeDetails
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Query("""
        SELECT * FROM episodes
        ORDER BY episode
    """)
    fun observeAll(): Flow<List<EpisodeEntity>>

    @Transaction
    @Query("""
        SELECT * FROM episodes
        WHERE id = :episodeId
        LIMIT 1
    """)
    fun observeDetails(episodeId: Long): Flow<EpisodeDetails?>

    @Query("""
        SELECT * FROM episodes
        WHERE favorite = 1
        ORDER BY name COLLATE NOCASE
    """)
    fun observeFavorites(): Flow<List<EpisodeEntity>>

    @Query("""
        UPDATE episodes
        SET favorite = :favorite
        WHERE id = :episodeId
    """)
    suspend fun setFavorite(episodeId: Long, favorite: Boolean)
}