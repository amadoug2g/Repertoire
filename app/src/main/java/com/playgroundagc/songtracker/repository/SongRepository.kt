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

    val readAllData: Flow<List<Song>> = songDao.readAllData()

    suspend fun addSong(song: Song){
        songDao.addSong(song)
    }

}