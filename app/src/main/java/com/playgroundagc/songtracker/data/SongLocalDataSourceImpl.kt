package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.app.framework.SongDao
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import com.playgroundagc.songtracker.extension.copyToClipboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Amadou on 03/08/2021, 14:57
 *
 * Local DataSource Implementation
 *
 */

class SongLocalDataSourceImpl(private val songDao: SongDao) : SongLocalDataSource {
    override fun readAllSongs(): Flow<List<Song>> {
        return songDao.readAllSongs()
    }

    override suspend fun add(song: Song) {
        songDao.addSong(song)
    }

    override suspend fun update(song: Song) {
        songDao.updateSong(song)
    }

    override suspend fun delete(song: Song) {
        songDao.deleteSong(song)
    }

    override suspend fun deleteAll() {
        songDao.deleteAllSongs()
    }

    override suspend fun copy(context: Context) {
        var result = ""
        readAllSongs().collect { value ->
            value.forEach {
                val current = "${it.name}, ${it.artist}, ${it.status}\n"
                result += current
            }

            context.copyToClipboard(result)
        }
    }

    override fun readNotStarted(status: SongStatus): Flow<List<Song>> {
        return songDao.readStatusDataDESC(SongStatus.Not_Started)
    }

    override fun readLearned(status: SongStatus): Flow<List<Song>> {
        return songDao.readStatusDataDESC(SongStatus.Learned)
    }

    override fun readInProgress(status: SongStatus): Flow<List<Song>> {
        return songDao.readStatusDataDESC(SongStatus.In_Progress)
    }

    override fun countNotStarted(status: SongStatus): Int {
        return songDao.countSongsByStatus(SongStatus.Not_Started)
    }

    override fun countLearned(status: SongStatus): Int {
        return songDao.countSongsByStatus(SongStatus.Learned)
    }

    override fun countInProgress(status: SongStatus): Int {
        return songDao.countSongsByStatus(SongStatus.In_Progress)
    }

}