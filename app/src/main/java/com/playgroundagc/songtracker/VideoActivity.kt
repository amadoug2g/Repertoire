package com.playgroundagc.songtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.playgroundagc.songtracker.databinding.ActivityVideoBinding
import com.playgroundagc.songtracker.util.Authentication
import org.jetbrains.anko.toast
import timber.log.Timber

class VideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private lateinit var binding: ActivityVideoBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)

        binding.youtubePlayer.initialize(Authentication.API_KEY, this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored && player != null) {
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            try {
                player.loadVideo(getVideoID())
                player.play()
            } catch (e: Exception) {
                toast("Error: No video available for this song")
            }
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        error: YouTubeInitializationResult?
    ) {
        toast("Error: $error")
    }

    private fun getVideoID(): String {
        val videoID = getBundle()
        return when (videoID.length) {
            0 -> {
                ""
            }
            11 -> {
                videoID
            }
            else -> {
                videoID.getVideoID()
            }
        }
    }

    private fun getBundle(): String {
        return intent.getStringExtra("videoID").orEmpty()
    }

    private fun String.getVideoID(): String {
        return if (this.length > 43)
            this.removePrefix(this.substringBefore("=")).removePrefix("=")
                .removeSuffix(this.substringAfter("&")).removeSuffix("&")
        else
            this.removePrefix("https://www.youtube.com/watch?v=")
    }
}