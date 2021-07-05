package com.playgroundagc.songtracker.data

import com.playgroundagc.songtracker.R

/**
 * Created by Amadou on 21/06/2021, 23:57
 *
 * Song Category Enum File
 *
 */

enum class SongCategory(val value: String) {
    Music("Music"),
    Movie_Shows("Movie / Shows"),
    Game("Game"),
    Anime("Anime");

    override fun toString() : String {
        return value
    }
}