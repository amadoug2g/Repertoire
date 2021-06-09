package com.playgroundagc.songtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playgroundagc.songtracker.data.SongStatus

/**
 * Created by Amadou on 07/06/2021, 17:25
 *
 * Song Class File
 *
 */

@Entity(tableName = "song_data")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val artist: String,
    var status: SongStatus
)


