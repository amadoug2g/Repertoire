package com.playgroundagc.songtracker.app.ui

import android.content.Context
import androidx.lifecycle.*
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
    private val addSongUseCase: AddSongUseCase,
    private val updateSongUseCase: UpdateSongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val deleteAllSongsUseCase: DeleteAllSongsUseCase,
    private val getSongsNotStartedUseCase: GetSongsNotStartedUseCase,
    private val getSongsInProgressUseCase: GetSongsInProgressUseCase,
    private val getSongsLearnedUseCase: GetSongsLearnedUseCase,
    private val countSongsNotStartedUseCase: CountSongsNotStartedUseCase,
    private val countSongsInProgressUseCase: CountSongsInProgressUseCase,
    private val countSongsLearnedUseCase: CountSongsLearnedUseCase,
    private val copySongsUseCase: CopySongsUseCase
) : ViewModel() {

    //region Variables

    private val _readStatus = MutableLiveData<Flow<List<DomainSong>>>()
    val readStatus: LiveData<Flow<List<DomainSong>>> = _readStatus

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
        _readStatus.postValue(getSongsNotStartedUseCase.invoke())

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
    private fun assignTabSelected(int: Int) {
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
            addSongUseCase.invoke(song)
//            AddSongUseCase(repo).invoke(song)
        }
    }

    fun updateSong(song: DomainSong) {
        viewModelScope.launch(IO) {
            updateSongUseCase.invoke(currentSong.value!!)
//            UpdateSongUseCase(repo).invoke(song)
        }

        assignSong(song)
    }

    fun deleteSong() {
        viewModelScope.launch(IO) {
            deleteSongUseCase.invoke(currentSong.value!!)
//            DeleteSongUseCase(repo).invoke(currentSong.value!!)
        }
    }

    fun deleteAllSongs() {
        viewModelScope.launch(IO) {
            deleteAllSongsUseCase.invoke()
//            DeleteAllSongsUseCase(repo).invoke()
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
                    getSongsNotStartedUseCase.invoke().collect {
                        assignSongList(it)
                    }
                }
                1 -> {
                    getSongsInProgressUseCase.invoke().collect {
                        assignSongList(it)
                    }
                }
                else -> {
                    getSongsLearnedUseCase.invoke().collect {
                        assignSongList(it)
                    }
                }
            }
        }
    }

    private fun getSongsNotStarted() {
        viewModelScope.launch(IO) {
//            readStatusNotStartedDataDESC = getSongsNotStartedUseCase.invoke()
//            readStatusNotStartedDataDESC = GetSongsNotStartedUseCase(repo).invoke()
        }
    }

    private fun getSongsInProgress() {
        viewModelScope.launch(IO) {
//            readStatusInProgressDataDESC = getSongsInProgressUseCase.invoke()
//            readStatusInProgressDataDESC = GetSongsInProgressUseCase(repo).invoke()
        }
    }

    private fun getSongsLearned() {
        viewModelScope.launch(IO) {
//            readStatusNotStartedDataDESC = getSongsLearnedUseCase.invoke()
//            readStatusNotStartedDataDESC = GetSongsLearnedUseCase(repo).invoke()
        }
    }

    fun countSongs() {
        countNotStartedSongs()
        countInProgressSongs()
        countLearnedSongs()
    }

    private fun countNotStartedSongs() {
        viewModelScope.launch(Default) {
            _countNotStartedSongs.postValue(countSongsNotStartedUseCase.invoke())
//            _countNotStartedSongs.postValue(CountSongsNotStartedUseCase(repo).invoke())
        }
    }

    private fun countInProgressSongs() {
        viewModelScope.launch(Default) {
            _countInProgressSongs.postValue(countSongsInProgressUseCase.invoke())
//            _countInProgressSongs.postValue(CountSongsInProgressUseCase(repo).invoke())
        }
    }

    private fun countLearnedSongs() {
        viewModelScope.launch(Default) {
            _countLearnedSongs.postValue(countSongsLearnedUseCase.invoke())
//            _countLearnedSongs.postValue(CountSongsLearnedUseCase(repo).invoke())
        }
    }

    fun copySongs(context: Context) {
        viewModelScope.launch(IO) {
            copySongsUseCase.invoke(context)
//            CopySongsUseCase(repo).invoke(context)
        }
    }
    //endregion
}

class SongViewModelFactory(
    private val addSong: AddSongUseCase,
    private val updateSongUseCase: UpdateSongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val deleteAllSongsUseCase: DeleteAllSongsUseCase,
    private val getSongsNotStartedUseCase: GetSongsNotStartedUseCase,
    private val getSongsInProgressUseCase: GetSongsInProgressUseCase,
    private val getSongsLearnedUseCase: GetSongsLearnedUseCase,
    private val countSongsNotStartedUseCase: CountSongsNotStartedUseCase,
    private val countSongsInProgressUseCase: CountSongsInProgressUseCase,
    private val countSongsLearnedUseCase: CountSongsLearnedUseCase,
    private val copySongsUseCase: CopySongsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass) as T
//        return SongViewModel(
//            addSong,
//            updateSongUseCase,
//            deleteSongUseCase,
//            deleteAllSongsUseCase,
//            getSongsNotStartedUseCase,
//            getSongsInProgressUseCase,
//            getSongsLearnedUseCase,
//            countSongsNotStartedUseCase,
//            countSongsInProgressUseCase,
//            countSongsLearnedUseCase,
//            copySongsUseCase
//        ) as T
        return modelClass.getConstructor(
            AddSongUseCase::class.java,
            UpdateSongUseCase::class.java,
            DeleteSongUseCase::class.java,
            DeleteAllSongsUseCase::class.java,
            GetSongsNotStartedUseCase::class.java,
            GetSongsInProgressUseCase::class.java,
            GetSongsLearnedUseCase::class.java,
            CountSongsNotStartedUseCase::class.java,
            CountSongsInProgressUseCase::class.java,
            CountSongsLearnedUseCase::class.java,
            CopySongsUseCase::class.java
        ).newInstance(
            addSong,
            updateSongUseCase,
            deleteSongUseCase,
            deleteAllSongsUseCase,
            getSongsNotStartedUseCase,
            getSongsInProgressUseCase,
            getSongsLearnedUseCase,
            countSongsNotStartedUseCase,
            countSongsInProgressUseCase,
            countSongsLearnedUseCase,
            copySongsUseCase
        )
    }
}