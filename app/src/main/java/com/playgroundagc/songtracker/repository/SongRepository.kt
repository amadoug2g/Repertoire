package com.playgroundagc.songtracker.repository

import com.playgroundagc.songtracker.data.SongDao
import com.playgroundagc.songtracker.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Created by Amadou on 07/06/2021, 17:42
 *
 * Song Tracker Repository
 *
 */

class SongRepository(private val songDao: SongDao) {

    val readAllDataASC: Flow<List<Song>> = songDao.readAllDataASC()
    val readAllDataByNameASC: Flow<List<Song>> = songDao.readAllDataByNameASC()
    val readAllDataByStatusASC: Flow<List<Song>> = songDao.readAllDataByStatusASC()
    val readAllDataDESC: Flow<List<Song>> = songDao.readAllDataDESC()
    val readAllDataByNameDESC: Flow<List<Song>> = songDao.readAllDataByNameDESC()
    val readAllDataByStatusDESC: Flow<List<Song>> = songDao.readAllDataByStatusDESC()

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
}