package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 03/08/2021, 14:40
 *
 * Local DataSource
 *
 */

interface SongLocalDataSource {
    fun readAllSongs(): Flow<List<Song>>

    suspend fun add(song: Song)
    suspend fun update(song: Song)
    suspend fun delete(song: Song)
    suspend fun deleteAll()
    suspend fun copy(context: Context)

    fun readNotStarted(status: SongStatus = SongStatus.Not_Started): Flow<List<Song>>
    fun readLearned(status: SongStatus = SongStatus.Learned): Flow<List<Song>>
    fun readInProgress(status: SongStatus = SongStatus.In_Progress): Flow<List<Song>>

    fun countNotStarted(status: SongStatus = SongStatus.Not_Started): Int
    fun countLearned(status: SongStatus = SongStatus.Learned): Int
    fun countInProgress(status: SongStatus = SongStatus.In_Progress): Int
}