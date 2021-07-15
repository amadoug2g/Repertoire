package com.playgroundagc.songtracker.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.playgroundagc.songtracker.getOrAwaitValue
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.model.SongCategory
import com.playgroundagc.songtracker.model.SongStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Amadou on 15/07/2021, 13:01
 *
 * [SongDao] Tests
 *
 */

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class SongDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: SongDatabase
    private lateinit var dao: SongDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SongDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.songDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addSong() = runBlockingTest {
        val songItem = Song(id = 1, "name", "artist", SongStatus.Not_Started, SongCategory.Music)

        dao.addSong(songItem)

        val list = dao.readStatusDataASC(songItem.status).asLiveData().getOrAwaitValue()

        assertThat(list).contains(songItem)
    }

    @Test
    fun updateSong() = runBlockingTest {
        var songItem = Song(id = 1, "name", "artist", SongStatus.Not_Started, SongCategory.Music)

        dao.addSong(songItem)

        songItem = Song(id = 1, "name", "artist", SongStatus.In_Progress, SongCategory.Music)

        dao.updateSong(songItem)

        val list = dao.readStatusDataASC(songItem.status).asLiveData().getOrAwaitValue()

        assertThat(list).contains(songItem)
    }

    @Test
    fun deleteSong() = runBlockingTest {
        val songItem = Song(id = 1, "name", "artist", SongStatus.Not_Started, SongCategory.Music)

        dao.addSong(songItem)
        dao.deleteSong(songItem)

        val list = dao.readStatusDataDESC(songItem.status).asLiveData().getOrAwaitValue()

        assertThat(list).doesNotContain(songItem)
    }

    @Test
    fun deleteAllSongs() = runBlockingTest {
        val songItem1 = Song(id = 1, "name", "artist", SongStatus.Not_Started, SongCategory.Music)
        val songItem2 = Song(id = 2, "name", "artist", SongStatus.In_Progress, SongCategory.Music)
        val songItem3 = Song(id = 3, "name", "artist", SongStatus.Learned, SongCategory.Music)

        dao.addSong(songItem1)
        dao.addSong(songItem2)
        dao.addSong(songItem3)

        dao.deleteAllSongs()

        val list = dao.readStatusDataDESC(SongStatus.Not_Started).asLiveData().getOrAwaitValue()

        assertThat(list).isEmpty()
    }

    @Test
    fun countSongsByStatus() = runBlockingTest {
        val songItem1 = Song(id = 1, "name", "artist", SongStatus.Not_Started, SongCategory.Music)
        val songItem2 = Song(id = 2, "name", "artist", SongStatus.In_Progress, SongCategory.Music)
        val songItem3 = Song(id = 3, "name", "artist", SongStatus.Learned, SongCategory.Music)

        dao.addSong(songItem1)
        dao.addSong(songItem2)
        dao.addSong(songItem3)

        val listNotStarted = dao.readStatusDataDESC(SongStatus.Not_Started).asLiveData().getOrAwaitValue()
        val listInProgress = dao.readStatusDataDESC(SongStatus.In_Progress).asLiveData().getOrAwaitValue()
        val listLearned = dao.readStatusDataDESC(SongStatus.Learned).asLiveData().getOrAwaitValue()

        val listCountNotStarted = dao.countSongsByStatus(SongStatus.Not_Started)
        val listCountInProgress = dao.countSongsByStatus(SongStatus.In_Progress)
        val listCountLearned = dao.countSongsByStatus(SongStatus.Learned)

        assertThat(listCountNotStarted).isEqualTo(listNotStarted.size)
        assertThat(listCountInProgress).isEqualTo(listInProgress.size)
        assertThat(listCountLearned).isEqualTo(listLearned.size)
    }
}