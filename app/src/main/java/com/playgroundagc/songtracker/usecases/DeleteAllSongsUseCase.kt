package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 17:51
 *
 * DeleteAllSongs UseCase
 *
 */

class DeleteAllSongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke() = songRepository.deleteAllSongs()
}