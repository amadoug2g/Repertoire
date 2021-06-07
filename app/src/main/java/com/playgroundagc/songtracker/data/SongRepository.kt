package com.playgroundagc.songtracker.data

import androidx.lifecycle.LiveData

/**
 * Created by Amadou on 07/06/2021, 17:42
 *
 * Song Tracker Repository
 *
 */

class SongRepository(private val songDao: SongDao) {

    val readAllData: LiveData<List<Song>> = songDao.readAllData()

    suspend fun addSong(song: Song){
        songDao.addSong(song)
    }

}