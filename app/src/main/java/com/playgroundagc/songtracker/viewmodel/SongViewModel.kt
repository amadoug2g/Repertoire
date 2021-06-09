package com.playgroundagc.songtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.data.SongDatabase
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * Song ViewModel File
 *
 */

class SongViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: Flow<List<Song>>
    private val repository: SongRepository

    init {
        val songDao = SongDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        readAllData = repository.readAllData
    }
    
    fun addSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(song)
        }
    }
}