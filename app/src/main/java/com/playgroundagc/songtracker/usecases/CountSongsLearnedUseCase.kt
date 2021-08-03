package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 17:58
 *
 * Count [Song]s Learned UseCase
 *
 */

class CountSongsLearnedUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Int = songRepository.countStatusLearned()
}