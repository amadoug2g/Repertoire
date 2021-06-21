package com.playgroundagc.songtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.data.SongDatabase
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.repository.SongRepository
import com.playgroundagc.songtracker.util.SingleLiveEvent
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

    //region Variables
//    val readAllDataASC: Flow<List<Song>>
//    val readAllDataByNameASC: Flow<List<Song>>
//    val readAllDataDESC: Flow<List<Song>>
//    val readAllDataByNameDESC: Flow<List<Song>>
//    val readAllDataByStatusDESC: Flow<List<Song>>
//    val readAllDataByStatusASC: Flow<List<Song>>
    val readStatusNotStartedDataASC: Flow<List<Song>>
    val readStatusNotStartedDataDESC: Flow<List<Song>>
    val readStatusInProgressDataASC: Flow<List<Song>>
    val readStatusInProgressDataDESC: Flow<List<Song>>
    val readStatusLearnedDataASC: Flow<List<Song>>
    val readStatusLearnedDataDESC: Flow<List<Song>>
    private val repository: SongRepository

    private val _currentSong = SingleLiveEvent<Song>()
    val currentSong : LiveData<Song>
        get() = _currentSong

    val tabSelect = MutableLiveData(0)
    val alphaSelect = MutableLiveData(true)
    val notStartedSelect = MutableLiveData(true)
    val inProgressSelect = MutableLiveData(true)
    val learnedSelect = MutableLiveData(true)
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
    }

    //region LiveData Assignments
    fun assignSong(currentSong: Song) {
        _currentSong.value = currentSong
    }

    fun assignSelection(int: Int) {
        tabSelect.value = int
    }

    fun assignAlphaSelect(value: Boolean) {
        alphaSelect.value = value
    }

    fun assignNotStartedSelect(value: Boolean) {
        notStartedSelect.value = value
    }

    fun assignInProgressSelect(value: Boolean) {
        inProgressSelect.value = value
    }

    fun assignLearnedSelect(value: Boolean) {
        learnedSelect.value = value
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
    //endregion
}