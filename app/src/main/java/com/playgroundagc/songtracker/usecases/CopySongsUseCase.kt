package com.playgroundagc.songtracker.usecases

import android.content.Context
import com.playgroundagc.songtracker.data.SongRepository
import com.playgroundagc.songtracker.domain.Song

/**
 * Created by Amadou on 23/07/2021, 18:38
 *
 * Copy [Song]s UseCase
 *
 */

class CopySongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(context: Context) = songRepository.copy(context)
}