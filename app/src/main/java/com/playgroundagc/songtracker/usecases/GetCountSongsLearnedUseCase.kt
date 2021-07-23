package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 17:58
 *
 * GetCountSongsLearned UseCase
 *
 */

class GetCountSongsLearnedUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Int = songRepository.countLearnedSongs()
}