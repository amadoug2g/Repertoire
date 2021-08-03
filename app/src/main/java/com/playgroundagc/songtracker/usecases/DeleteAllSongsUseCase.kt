package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 17:51
 *
 * Delete All [Song]s UseCase
 *
 */

class DeleteAllSongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke() = songRepository.deleteAll()
}