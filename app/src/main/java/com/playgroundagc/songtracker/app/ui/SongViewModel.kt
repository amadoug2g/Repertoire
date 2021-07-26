package com.playgroundagc.songtracker.app.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.app.framework.SongDatabase
import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.usecases.*
import com.playgroundagc.songtracker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.playgroundagc.songtracker.domain.Song as DomainSong

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * [DomainSong] ViewModel
 *
 */

class SongViewModel(
    application: Application
//    private val addSong: AddSongUseCase,
//    private val updateSongUseCase: UpdateSongUseCase,
//    private val deleteSongUseCase: DeleteSongUseCase,
//    private val deleteAllSongsUseCase: DeleteAllSongsUseCase,
//    private val getSongsNotStartedUseCase: GetSongsNotStartedUseCase,
//    private val getSongsInProgressUseCase: GetSongsInProgressUseCase,
//    private val getSongsLearnedUseCase: GetSongsLearnedUseCase,
//    private val getCountSongsNotStartedUseCase: GetCountSongsNotStartedUseCase,
//    private val getCountSongsInProgressUseCase: GetCountSongsInProgressUseCase,
//    private val getCountSongsLearnedUseCase: GetCountSongsLearnedUseCase,
//    private val copySongsUseCase: CopySongsUseCase
) : AndroidViewModel(application) {

    //region Variables
    var readStatusNotStartedDataDESC: Flow<List<DomainSong>>
    var readStatusInProgressDataDESC: Flow<List<DomainSong>>
    var readStatusLearnedDataDESC: Flow<List<DomainSong>>
    private val repository: SongRepository

    private val _countNotStartedSongs = MutableLiveData<Int>()
    val countNotStartedSongs: LiveData<Int> = _countNotStartedSongs

    private val _countInProgressSongs = MutableLiveData<Int>()
    val countInProgressSongs: LiveData<Int> = _countInProgressSongs

    private val _countLearnedSongs = MutableLiveData<Int>()
    val countLearnedSongs: LiveData<Int> = _countLearnedSongs

    private val _currentSongList = MutableLiveData<List<DomainSong>>()
    val currentSongList: LiveData<List<DomainSong>> = _currentSongList

    private val _currentSong = SingleLiveEvent<DomainSong>()
    val currentSong: LiveData<DomainSong> = _currentSong

    val tabSelect = MutableLiveData(0)
    //endregion

    init {
        val songDao = SongDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        readStatusNotStartedDataDESC = repository.readStatusNotStartedDataDESC()
        readStatusInProgressDataDESC = repository.readStatusInProgressDataDESC()
        readStatusLearnedDataDESC = repository.readStatusLearnedDataDESC()

        countSongs()
    }

    //region LiveData Assignments
    /**
     * Keeps track of current [DomainSong] for Details & Update Pages
     * */
    fun assignSong(currentSong: DomainSong) {
        _currentSong.postValue(currentSong)
    }

    /**
     * Keeps track of current tab
     * */
    fun assignTabSelected(int: Int) {
        tabSelect.postValue(int)
    }

    /**
     * Keeps track of current [DomainSong] list shown
     * */
    private fun assignSongList(list: List<DomainSong>) {
        _currentSongList.postValue(list)
    }
    //endregion

    //region Use Cases
    fun addSong(song: DomainSong) {
        viewModelScope.launch(IO) {
//            addSong.invoke(song)
            AddSongUseCase(repository).invoke(song)
        }
    }

    fun updateSong(song: DomainSong) {
        viewModelScope.launch(IO) {
//            updateSongUseCase.invoke(currentSong.value!!)
            UpdateSongUseCase(repository).invoke(song)
        }

        assignSong(song)
    }

    fun deleteSong() {
        viewModelScope.launch(IO) {
//            deleteSongUseCase.invoke(currentSong.value!!)
            DeleteSongUseCase(repository).invoke(currentSong.value!!)
        }
    }

    fun deleteAllSongs() {
        viewModelScope.launch(IO) {
//            deleteAllSongsUseCase.invoke()
            DeleteAllSongsUseCase(repository).invoke()
        }
    }

    fun getSongs() {
        getSongsNotStarted()
        getSongsInProgress()
        getSongsLearned()
    }

    fun getSongList(status: Int) {
        assignTabSelected(status)
        viewModelScope.launch(IO) {
            when (status) {
                0 -> {
                    readStatusNotStartedDataDESC.collect {
                        assignSongList(it)
                    }
                }
                1 -> {
                    readStatusInProgressDataDESC.collect {
                        assignSongList(it)
                    }
                }
                else -> {
                    readStatusLearnedDataDESC.collect {
                        assignSongList(it)
                    }
                }
            }
        }
    }

    private fun getSongsNotStarted() {
        viewModelScope.launch(IO) {
//            readStatusNotStartedDataDESC = getSongsNotStartedUseCase.invoke()
            readStatusNotStartedDataDESC = GetSongsNotStartedUseCase(repository).invoke()
        }
    }

    private fun getSongsInProgress() {
        viewModelScope.launch(IO) {
//            readStatusInProgressDataDESC = getSongsInProgressUseCase.invoke()
            readStatusInProgressDataDESC = GetSongsInProgressUseCase(repository).invoke()
        }
    }

    private fun getSongsLearned() {
        viewModelScope.launch(IO) {
//            readStatusNotStartedDataDESC = getSongsLearnedUseCase.invoke()
            readStatusNotStartedDataDESC = GetSongsLearnedUseCase(repository).invoke()
        }
    }

    fun countSongs() {
        countNotStartedSongs()
        countInProgressSongs()
        countLearnedSongs()
    }

    private fun countNotStartedSongs() {
        viewModelScope.launch(Default) {
//            _countNotStartedSongs.postValue(getCountSongsNotStartedUseCase.invoke())
            _countNotStartedSongs.postValue(GetCountSongsNotStartedUseCase(repository).invoke())
        }
    }

    private fun countInProgressSongs() {
        viewModelScope.launch(Default) {
//            _countInProgressSongs.postValue(getCountSongsInProgressUseCase.invoke())
            _countInProgressSongs.postValue(GetCountSongsInProgressUseCase(repository).invoke())
        }
    }

    private fun countLearnedSongs() {
        viewModelScope.launch(Default) {
//            _countLearnedSongs.postValue(getCountSongsLearnedUseCase.invoke())
            _countLearnedSongs.postValue(GetCountSongsLearnedUseCase(repository).invoke())
        }
    }

    fun copySongs(context: Context) {
        viewModelScope.launch(IO) {
//            copySongsUseCase.invoke(context)
            CopySongsUseCase(repository).invoke(context)
        }
    }
    //endregion
}

//class SongViewModelFactory(
//    application: Application,
//    private val addSong: AddSongUseCase,
//    private val updateSongUseCase: UpdateSongUseCase,
//    private val deleteSongUseCase: DeleteSongUseCase,
//    private val deleteAllSongsUseCase: DeleteAllSongsUseCase,
//    private val getSongsNotStartedUseCase: GetSongsNotStartedUseCase,
//    private val getSongsInProgressUseCase: GetSongsInProgressUseCase,
//    private val getSongsLearnedUseCase: GetSongsLearnedUseCase,
//    private val getCountSongsNotStartedUseCase: GetCountSongsNotStartedUseCase,
//    private val getCountSongsInProgressUseCase: GetCountSongsInProgressUseCase,
//    private val getCountSongsLearnedUseCase: GetCountSongsLearnedUseCase,
//    private val copySongsUseCase: CopySongsUseCase
//) : ViewModelProvider.AndroidViewModelFactory(application) {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass) as T
////        return SongViewModel(application = Application(), addSong, updateSongUseCase, deleteSongUseCase, deleteAllSongsUseCase, getSongsNotStartedUseCase, getSongsInProgressUseCase, getSongsLearnedUseCase, getCountSongsNotStartedUseCase, getCountSongsInProgressUseCase, getCountSongsLearnedUseCase, copySongsUseCase) as T
//    }
//}