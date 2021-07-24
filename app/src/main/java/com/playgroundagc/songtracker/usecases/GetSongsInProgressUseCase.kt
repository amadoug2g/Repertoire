package com.playgroundagc.songtracker.usecases

import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 17:54
 *
 * GetSongsInProgress UseCase
 *
 */

class GetSongsInProgressUseCase(private val songRepository: SongRepository) {
    operator fun invoke() = songRepository.readStatusInProgressDataDESC()
}