package com.playgroundagc.songtracker.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.app.framework.SongDatabase
import com.playgroundagc.songtracker.data.SongLocalDataSourceImpl
import com.playgroundagc.songtracker.data.SongRepositoryImpl
import com.playgroundagc.songtracker.databinding.ActivityMainBinding
import com.playgroundagc.songtracker.usecases.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

//    val viewModel by viewModels<SongViewModel>()

    companion object {
        private lateinit var binding: ActivityMainBinding
        lateinit var navController: NavController
        lateinit var viewModel: SongViewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        val songDao = SongDatabase.getDatabase(applicationContext).songDao()
        val localDataSourceImpl = SongLocalDataSourceImpl(songDao)
        val repository = SongRepositoryImpl(localDataSourceImpl)
        viewModel = ViewModelProvider(
            this, SongViewModelFactory(
                AddSongUseCase(repository),
                UpdateSongUseCase(repository),
                DeleteSongUseCase(repository),
                DeleteAllSongsUseCase(repository),
                GetSongsNotStartedUseCase(repository),
                GetSongsInProgressUseCase(repository),
                GetSongsLearnedUseCase(repository),
                CountSongsNotStartedUseCase(repository),
                CountSongsInProgressUseCase(repository),
                CountSongsLearnedUseCase(repository),
                CopySongsUseCase(repository)
            )
        ).get(SongViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }

        initializeTimber()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(findNavController(R.id.fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    //endregion

    //region Initialization
    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    //endregion
}