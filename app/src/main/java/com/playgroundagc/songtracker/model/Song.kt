package com.playgroundagc.songtracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.data.SongCategory
import com.playgroundagc.songtracker.data.SongStatus
import kotlinx.parcelize.Parcelize

/**
 * Created by Amadou on 07/06/2021, 17:25
 *
 * [Song] Class File
 *
 */

@Parcelize
@Entity(tableName = "song_data")
data class Song(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var artist: String,
    var status: SongStatus,
    var category: SongCategory
): Parcelable {
    val imageCategory: Int
        get() {
            return when (category) {
                SongCategory.Music -> R.drawable.loudspeaker_icon
                SongCategory.Movie_Shows -> R.drawable.video_camera_icon
                SongCategory.Game -> R.drawable.game_console_icon
                SongCategory.Anime -> R.drawable.ninja
            }
        }

}


