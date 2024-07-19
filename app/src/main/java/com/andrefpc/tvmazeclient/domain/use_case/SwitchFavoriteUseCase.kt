package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(show: Show) {
        return if (showRoomRepository.isFavorite(show.id)) {
            showRoomRepository.delete(show.id)
        } else {
            showRoomRepository.insert(show)
        }
    }
}