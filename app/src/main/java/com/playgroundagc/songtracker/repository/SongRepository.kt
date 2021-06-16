package com.playgroundagc.songtracker.repository

import androidx.lifecycle.LiveData
import com.playgroundagc.songtracker.data.SongDao
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.util.SingleLiveEvent
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

    suspend fun updateSong(song: Song) {
        songDao.updateSong(song)
    }
}