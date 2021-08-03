package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 17:52
 *
 * Get [Song]s Not Started UseCase
 *
 */

class GetSongsNotStartedUseCase(private val songRepository: SongRepository) {
    operator fun invoke() = songRepository.readStatusNotStarted()
}