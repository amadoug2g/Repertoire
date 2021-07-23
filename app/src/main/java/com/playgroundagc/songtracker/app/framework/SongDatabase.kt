package com.playgroundagc.songtracker.app.framework

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.playgroundagc.songtracker.domain.Song


/**
 * Created by Amadou on 07/06/2021, 17:37
 *
 * Room Database File
 *
 */

@Database(
    entities = [Song::class],
    version = 3,
    exportSchema = true
)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        private val MIGRATION_2_3: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE song_data ADD COLUMN link TEXT NOT NULL DEFAULT ''"
//                    "ALTER TABLE song_data ADD COLUMN category TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getDatabase(context: Context): SongDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}