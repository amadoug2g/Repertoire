package com.playgroundagc.songtracker.data.local

import androidx.room.*
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.model.SongStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 07/06/2021, 17:35
 *
 * [Song] Data Access Object
 *
 */

@Dao
interface SongDao {
    /**
     * Adding a [Song] to the database. If it already exists, ignore it.
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: Song)

    /**
     * Update a [Song] from the database
     * */
    @Update
    suspend fun updateSong(song: Song)

    /**
     * Delete a [Song] from the database
     * */
    @Delete
    suspend fun deleteSong(song: Song)

    /**
     * Delete every [Song] from the database
     * */
    @Query("DELETE FROM song_data")
    suspend fun deleteAllSongs()

    /**
     * Returns every [Song] from the database
     * */
    @Query("SELECT * FROM song_data ORDER BY status DESC, id DESC")
    fun readAllSongs(): Flow<List<Song>>

    /**
     * Returns every [Song] from the database, sorted in ascending order & filtered by [SongStatus]
     * */
    @Query("SELECT * FROM song_data WHERE status = :status ORDER BY id ASC")
    fun readStatusDataASC(status: SongStatus): Flow<List<Song>>

    /**
     * Returns every [Song] from the database, sorted in descending order & filtered by [SongStatus]
     * */
    @Query("SELECT * FROM song_data WHERE status = :status ORDER BY id DESC")
    fun readStatusDataDESC(status: SongStatus): Flow<List<Song>>

    /**
     * Returns [Song] count from the database, filtered by [SongStatus]
     * */
    @Query("SELECT COUNT(*) FROM song_data WHERE status = :status")
    fun countSongsByStatus(status: SongStatus): Int
}