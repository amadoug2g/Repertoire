package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 17:49
 *
 * Update [Song] UseCase
 *
 */

class UpdateSongUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(song: Song) = songRepository.update(song)
}