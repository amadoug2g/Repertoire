package com.playgroundagc.songtracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Amadou on 07/06/2021, 17:35
 *
 * Song Data Access Object
 *
 */

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: Song)

    @Query("SELECT * FROM song_data ORDER BY id ASC")
    fun readAllData(): LiveData<List<Song>>
}