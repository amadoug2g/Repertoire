package com.playgroundagc.songtracker.data

import android.content.Context
import com.playgroundagc.songtracker.app.framework.SongDao
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongStatus
import com.playgroundagc.songtracker.extension.copyToClipboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Amadou on 07/06/2021, 17:42
 *
 * [Song] Repository
 *
 */

class SongRepository(private val songDao: SongDao) {

    val readStatusNotStartedDataASC: Flow<List<Song>> = songDao.readStatusDataASC(SongStatus.Not_Started)
    val readStatusInProgressDataASC: Flow<List<Song>> = songDao.readStatusDataASC(SongStatus.In_Progress)
    val readStatusLearnedDataASC: Flow<List<Song>> = songDao.readStatusDataASC(SongStatus.Learned)
    val readStatusNotStartedDataDESC: Flow<List<Song>> = songDao.readStatusDataDESC(SongStatus.Not_Started)
    val readStatusInProgressDataDESC: Flow<List<Song>> = songDao.readStatusDataDESC(SongStatus.In_Progress)
    val readStatusLearnedDataDESC: Flow<List<Song>> = songDao.readStatusDataDESC(SongStatus.Learned)
    val readAllSongs: Flow<List<Song>> = songDao.readAllSongs()
//    val countNotStartedSongs: Int = songDao.countSongsByStatus(SongStatus.Not_Started)
//    val countInProgressSongs: Int = songDao.countSongsByStatus(SongStatus.In_Progress)
//    val countLearnedSongs: Int = songDao.countSongsByStatus(SongStatus.Learned)

    suspend fun addSong(song: Song){
        songDao.addSong(song)
    }

    suspend fun updateSong(song: Song) {
        songDao.updateSong(song)
    }

    suspend fun deleteSong(song: Song) {
        songDao.deleteSong(song)
    }

    suspend fun deleteAllSongs() {
        songDao.deleteAllSongs()
    }

    fun countNotStartedSongs(): Int {
        return songDao.countSongsByStatus(SongStatus.Not_Started)
    }

    fun countInProgressSongs(): Int {
        return songDao.countSongsByStatus(SongStatus.In_Progress)
    }

    fun countLearnedSongs(): Int {
        return songDao.countSongsByStatus(SongStatus.Learned)
    }

    suspend fun copySongs(context: Context) {
        var result = ""
        readAllSongs.collect { value ->
            value.forEach {
                val current = "${it.name}, ${it.artist}, ${it.status}\n"
                result += current
            }

            context.copyToClipboard(result)
        }
    }
}