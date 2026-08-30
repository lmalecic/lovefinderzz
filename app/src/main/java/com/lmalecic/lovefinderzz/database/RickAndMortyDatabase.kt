package com.lmalecic.lovefinderzz.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lmalecic.lovefinderzz.dao.CharacterDao
import com.lmalecic.lovefinderzz.dao.EpisodeDao
import com.lmalecic.lovefinderzz.dao.LocationDao
import com.lmalecic.lovefinderzz.dao.SyncDao
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterEpisodeCrossReference
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.entity.LocationEntity

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class,
        EpisodeEntity::class,
        CharacterEpisodeCrossReference::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2
        ),
        AutoMigration(
            from = 2,
            to = 3,
        )
    ]
)
@TypeConverters(RoomConverters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun syncDao(): SyncDao
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun episodeDao(): EpisodeDao

    companion object {
        @Volatile
        private var instance: RickAndMortyDatabase? = null

        fun getInstance(context: Context): RickAndMortyDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RickAndMortyDatabase::class.java,
                    "rick_and_morty.db"
                ).build().also {
                    instance = it
                }
            }
        }
    }
}