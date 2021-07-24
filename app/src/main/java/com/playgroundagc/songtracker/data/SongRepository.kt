package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 07/06/2021, 17:42
 *
 * [Song] Repository
 *
 */

class SongRepository(private val localSongRepository: LocalSongRepository) {
    val readAllSongs: Flow<List<Song>> = localSongRepository.readAllSongs()

    suspend fun addSong(song: Song) {
        localSongRepository.addSong(song)
    }

    suspend fun updateSong(song: Song) {
        localSongRepository.updateSong(song)
    }

    suspend fun deleteSong(song: Song) {
        localSongRepository.deleteSong(song)
    }

    suspend fun deleteAllSongs() {
        localSongRepository.deleteAllSongs()
    }

    suspend fun copySongs(context: Context) {
        localSongRepository.copySongs(context)
    }

    fun readStatusNotStartedDataDESC(): Flow<List<Song>> {
        return localSongRepository.readStatusDataDESC(SongStatus.Not_Started)
    }

    fun readStatusInProgressDataDESC(): Flow<List<Song>> {
        return localSongRepository.readStatusDataDESC(SongStatus.In_Progress)
    }

    fun readStatusLearnedDataDESC(): Flow<List<Song>> {
        return localSongRepository.readStatusDataDESC(SongStatus.Learned)
    }

    fun countNotStartedSongs(): Int {
        return localSongRepository.countSongsByStatus(SongStatus.Not_Started)
    }

    fun countInProgressSongs(): Int {
        return localSongRepository.countSongsByStatus(SongStatus.In_Progress)
    }

    fun countLearnedSongs(): Int {
        return localSongRepository.countSongsByStatus(SongStatus.Learned)
    }
}

interface LocalSongRepository {
    fun readAllSongs(): Flow<List<Song>>

    suspend fun addSong(song: Song)
    suspend fun updateSong(song: Song)
    suspend fun deleteSong(song: Song)
    suspend fun deleteAllSongs()
    suspend fun copySongs(context: Context)

    fun readStatusDataDESC(status: SongStatus): Flow<List<Song>>

    fun countSongsByStatus(status: SongStatus): Int
}