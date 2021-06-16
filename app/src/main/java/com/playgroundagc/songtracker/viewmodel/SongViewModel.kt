package com.playgroundagc.songtracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.data.SongDatabase
import com.playgroundagc.songtracker.data.SongStatus
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.repository.SongRepository
import com.playgroundagc.songtracker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * Song ViewModel File
 *
 */

class SongViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: Flow<List<Song>>
    private val repository: SongRepository

    private val _currentSong = SingleLiveEvent<Song>()
    val currentSong : LiveData<Song>
        get() = _currentSong


    init {
        val songDao = SongDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        readAllData = repository.readAllData
    }

    fun assignSong(currentSong: Song) {
        _currentSong.value = currentSong
    }
    
    fun addSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(song)
        }
    }

    fun updateSong() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSong(currentSong.value!!)
        }
    }

    fun deleteSong() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSong(currentSong.value!!)
        }
    }

    fun deleteAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSongs()
        }
    }
}