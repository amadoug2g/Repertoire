package com.playgroundagc.songtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.data.local.SongDatabase
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.repository.SongRepository
import com.playgroundagc.songtracker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * [Song] ViewModel
 *
 */

class SongViewModel(application: Application): AndroidViewModel(application) {

    //region Variables
    private val readStatusNotStartedDataASC: Flow<List<Song>>
    val readStatusNotStartedDataDESC: Flow<List<Song>>
    private val readStatusInProgressDataASC: Flow<List<Song>>
    val readStatusInProgressDataDESC: Flow<List<Song>>
    private val readStatusLearnedDataASC: Flow<List<Song>>
    val readStatusLearnedDataDESC: Flow<List<Song>>
    private val repository: SongRepository

//    val countNotStartedSongs: Int
//    val countInProgressSongs: Int
//    val countLearnedSongs: Int


    private val _countNotStartedSongs = MutableLiveData<Int>()
    val countNotStartedSongs : LiveData<Int> = _countNotStartedSongs

    private val _countInProgressSongs = MutableLiveData<Int>()
    val countInProgressSongs : LiveData<Int> = _countInProgressSongs

    private val _countLearnedSongs = MutableLiveData<Int>()
    val countLearnedSongs : LiveData<Int> = _countLearnedSongs

    private val _currentSong = SingleLiveEvent<Song>()
    val currentSong : LiveData<Song> = _currentSong

    val tabSelect = MutableLiveData(0)
    //endregion

    init {
        val songDao = SongDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        readStatusNotStartedDataASC = repository.readStatusNotStartedDataASC
        readStatusInProgressDataASC = repository.readStatusInProgressDataASC
        readStatusLearnedDataASC = repository.readStatusLearnedDataASC
        readStatusNotStartedDataDESC = repository.readStatusNotStartedDataDESC
        readStatusInProgressDataDESC = repository.readStatusInProgressDataDESC
        readStatusLearnedDataDESC = repository.readStatusLearnedDataDESC

        countNotStartedSongs()
        countInProgressSongs()
        countLearnedSongs()
    }

    //region LiveData Assignments
    /**
     * Keeps track of current [Song] for Details & Update Pages
     * */
    fun assignSong(currentSong: Song) {
        _currentSong.value = currentSong
    }

    /**
     * Keeps track of current tab
     * */
    fun assignTabSelected(int: Int) {
        tabSelect.value = int
    }
    //endregion

    //region Database Operations
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

    private fun countNotStartedSongs() {
        viewModelScope.launch(Dispatchers.Default) {
            _countNotStartedSongs.postValue(repository.countNotStartedSongs())
        }
    }

    private fun countInProgressSongs() {
        viewModelScope.launch(Dispatchers.Default) {
            _countInProgressSongs.postValue(repository.countInProgressSongs())
        }
    }

    private fun countLearnedSongs() {
        viewModelScope.launch(Dispatchers.Default) {
            _countLearnedSongs.postValue(repository.countLearnedSongs())
        }
    }
    //endregion
}