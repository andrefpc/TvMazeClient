package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.room.ShowRoomRepository
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(show: Show) {
        return if(showRoomRepository.isFavorite(show.id)){
            showRoomRepository.delete(show.id)
        }else{
            showRoomRepository.insert(show)
        }
    }
}