package com.playgroundagc.songtracker.app.framework

import android.content.Context
import androidx.room.*
import com.playgroundagc.songtracker.data.LocalSongRepository
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import com.playgroundagc.songtracker.extension.copyToClipboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Amadou on 07/06/2021, 17:35
 *
 * [Song] Data Access Object
 *
 */

@Dao
interface SongDao : LocalSongRepository {
    /**
     * Adding a [Song] to the database. If it already exists, ignore it.
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addSong(song: Song)

    /**
     * Update a [Song] from the database
     * */
    @Update
    override suspend fun updateSong(song: Song)

    /**
     * Delete a [Song] from the database
     * */
    @Delete
    override suspend fun deleteSong(song: Song)

    /**
     * Delete every [Song] from the database
     * */
    @Query("DELETE FROM song_data")
    override suspend fun deleteAllSongs()

    /**
     * Returns every [Song] from the database
     * */
    @Query("SELECT * FROM song_data ORDER BY status DESC, id DESC")
    override fun readAllSongs(): Flow<List<Song>>

    /**
     * Returns every [Song] from the database, sorted in ascending order & filtered by [SongStatus]
     * */
    @Query("SELECT * FROM song_data WHERE status = :status ORDER BY id ASC")
    fun readStatusDataASC(status: SongStatus): Flow<List<Song>>

    /**
     * Returns every [Song] from the database, sorted in descending order & filtered by [SongStatus]
     * */
    @Query("SELECT * FROM song_data WHERE status = :status ORDER BY id DESC")
    override fun readStatusDataDESC(status: SongStatus): Flow<List<Song>>

    /**
     * Returns [Song] count from the database, filtered by [SongStatus]
     * */
    @Query("SELECT COUNT(*) FROM song_data WHERE status = :status")
    override fun countSongsByStatus(status: SongStatus): Int

    override suspend fun copySongs(context: Context) {
        var result = ""
        readAllSongs().collect { value ->
            value.forEach {
                val current = "${it.name}, ${it.artist}, ${it.status}\n"
                result += current
            }

            context.copyToClipboard(result)
        }
    }
}