package com.lmalecic.lovefinderzz.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.lmalecic.lovefinderzz.entity.ReminderDetails
import com.lmalecic.lovefinderzz.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun insert(reminder: ReminderEntity): Long

    @Transaction
    @Query("""
        SELECT * FROM reminders
        WHERE enabled = 1
            AND triggerAtEpochMillis > :now
        ORDER BY triggerAtEpochMillis
    """)
    fun observeUpcomingDetails(now: Long): Flow<List<ReminderDetails>>

    @Transaction
    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getDetailsById(id: Long): ReminderDetails?

    @Query("""
        SELECT * FROM reminders
        WHERE enabled = 1
            AND triggerAtEpochMillis > :now
        ORDER BY triggerAtEpochMillis
    """)
    suspend fun getUpcoming(now: Long): List<ReminderEntity>

    @Query("""
        UPDATE reminders
        SET enabled = 0,
            deliveredAtEpochMillis = :deliveredAt
        WHERE id = :id
    """)
    suspend fun markAsDelivered(id: Long, deliveredAt: Long)

    @Delete
    suspend fun delete(reminder: ReminderEntity)
}