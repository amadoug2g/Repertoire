package com.playgroundagc.songtracker.usecases

import android.content.Context
import com.playgroundagc.songtracker.data.SongRepository

/**
 * Created by Amadou on 23/07/2021, 18:38
 *
 * CopySongs UseCase
 *
 */

class CopySongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(context: Context) = songRepository.copySongs(context)
}