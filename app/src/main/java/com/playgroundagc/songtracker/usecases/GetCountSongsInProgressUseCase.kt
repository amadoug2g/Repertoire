package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 17:57
 *
 * GetCountSongsInProgress UseCase
 *
 */

class GetCountSongsInProgressUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Int = songRepository.countInProgressSongs()
}