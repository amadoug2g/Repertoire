package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 17:57
 *
 * Count [Song]s In Progress UseCase
 *
 */

class CountSongsInProgressUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Int = songRepository.countStatusInProgress()
}