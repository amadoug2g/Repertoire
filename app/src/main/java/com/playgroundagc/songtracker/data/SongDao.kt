package com.playgroundagc.songtracker.data

import androidx.room.*
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

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM song_data")
    suspend fun deleteAllSongs()

    @Query("SELECT * FROM song_data ORDER BY id DESC")
    fun readAllData(): Flow<List<Song>>
}