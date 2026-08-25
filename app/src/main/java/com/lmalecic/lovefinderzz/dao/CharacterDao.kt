package com.lmalecic.lovefinderzz.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.lmalecic.lovefinderzz.entity.CharacterDetails
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("""
        SELECT * FROM characters
        ORDER BY name COLLATE NOCASE
    """)
    fun observeAll(): Flow<List<CharacterEntity>>

    @Transaction
    @Query("""
        SELECT * FROM characters
        WHERE id = :characterId
        LIMIT 1
    """)
    fun observeDetails(characterId: Long): Flow<CharacterDetails?>
}