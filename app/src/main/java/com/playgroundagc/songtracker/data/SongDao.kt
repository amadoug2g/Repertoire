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

    @Query("SELECT * FROM song_data ORDER BY id ASC")
    fun readAllDataASC(): Flow<List<Song>>

    @Query("SELECT * FROM song_data ORDER BY id DESC")
    fun readAllDataDESC(): Flow<List<Song>>

    @Query("SELECT * FROM song_data ORDER BY name ASC")
    fun readAllDataByNameASC(): Flow<List<Song>>

    @Query("SELECT * FROM song_data ORDER BY name DESC")
    fun readAllDataByNameDESC(): Flow<List<Song>>

    @Query("SELECT * FROM song_data ORDER BY status ASC")
    fun readAllDataByStatusASC(): Flow<List<Song>>

    @Query("SELECT * FROM song_data ORDER BY status DESC")
    fun readAllDataByStatusDESC(): Flow<List<Song>>
}