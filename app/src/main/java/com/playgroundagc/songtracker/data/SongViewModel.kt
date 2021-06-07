package com.playgroundagc.songtracker.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * TODO: File Description
 *
 */
class SongViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Song>>
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