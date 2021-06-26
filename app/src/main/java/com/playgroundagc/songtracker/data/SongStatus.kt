package com.playgroundagc.songtracker.data

/**
 * Created by Amadou on 07/06/2021, 17:26
 *
 * Song Status Enum File
 *
 */

enum class SongStatus(val value: String) {
    Not_Started("Not Started"),
    In_Progress("In Progress"),
    Learned("Learned");

    override fun toString() : String {
        return value
    }
}