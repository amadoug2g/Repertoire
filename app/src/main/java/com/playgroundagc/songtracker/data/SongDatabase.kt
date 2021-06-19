package com.playgroundagc.songtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.playgroundagc.songtracker.model.Song

/**
 * Created by Amadou on 07/06/2021, 17:37
 *
 * Room Database File
 *
 */

@Database(entities = [Song::class], version = 1, exportSchema = true)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}