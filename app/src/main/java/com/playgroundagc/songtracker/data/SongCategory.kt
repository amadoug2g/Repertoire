package com.playgroundagc.songtracker.data

import com.playgroundagc.songtracker.R

/**
 * Created by Amadou on 21/06/2021, 23:57
 *
 * Song Category Enum File
 *
 */

enum class SongCategory(val value: String, val path: String) {
    Music("Music", "loudspeaker_icon"),
    Movie_Shows("Movie / Shows", "video_camera_icon"),
    Game("Game", "game_console_icon"),
    Anime("Anime", "repertoire_logo");

    override fun toString() : String {
        return value
    }
}