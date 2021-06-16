package com.playgroundagc.songtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.util.SingleLiveEvent
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

    @Query("SELECT * FROM song_data ORDER BY id DESC")
    fun readAllData(): Flow<List<Song>>
}