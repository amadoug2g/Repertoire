package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 03/08/2021, 14:46
 *
 * [Song] Repository Interface
 *
 */

interface SongRepository {
    suspend fun add(song: Song)
    suspend fun update(song: Song)
    suspend fun delete(song: Song)
    suspend fun deleteAll()
    suspend fun copy(context: Context)

    fun readStatusNotStarted(status: SongStatus = SongStatus.Not_Started): Flow<List<Song>>
    fun readStatusLearned(status: SongStatus = SongStatus.Learned): Flow<List<Song>>
    fun readStatusInProgress(status: SongStatus = SongStatus.In_Progress): Flow<List<Song>>

    fun countStatusNotStarted(status: SongStatus = SongStatus.Not_Started): Int
    fun countStatusLearned(status: SongStatus = SongStatus.Learned): Int
    fun countStatusInProgress(status: SongStatus = SongStatus.In_Progress): Int
}