package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 03/08/2021, 15:16
 *
 * [Song] Repository Implementation
 *
 */

class SongRepositoryImpl(private val localDataSource: SongLocalDataSource) : SongRepository {
    override suspend fun add(song: Song) {
        localDataSource.add(song)
    }

    override suspend fun update(song: Song) {
        localDataSource.update(song)
    }

    override suspend fun delete(song: Song) {
        localDataSource.delete(song)
    }

    override suspend fun deleteAll() {
        localDataSource.deleteAll()
    }

    override suspend fun copy(context: Context) {
        localDataSource.copy(context)
    }

    override fun readStatusNotStarted(status: SongStatus): Flow<List<Song>> {
        return localDataSource.readNotStarted()
    }

    override fun readStatusLearned(status: SongStatus): Flow<List<Song>> {
        return localDataSource.readLearned()
    }

    override fun readStatusInProgress(status: SongStatus): Flow<List<Song>> {
        return localDataSource.readInProgress()
    }

    override fun countStatusNotStarted(status: SongStatus): Int {
        return localDataSource.countNotStarted()
    }

    override fun countStatusLearned(status: SongStatus): Int {
        return localDataSource.countLearned()
    }

    override fun countStatusInProgress(status: SongStatus): Int {
        return localDataSource.countInProgress()
    }
}