package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 17:57
 *
 * GetCountSongsNotStarted UseCase
 *
 */

class GetCountSongsNotStartedUseCase(private val songRepository: SongRepository) {
    operator fun invoke() = songRepository.countNotStartedSongs()
}