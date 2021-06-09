package com.playgroundagc.songtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playgroundagc.songtracker.model.Song
import kotlinx.coroutines.flow.Flow

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
    fun readAllData(): Flow<List<Song>>
}