package com.andrefpc.tvmazeclient.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.launch

/**
 * ViewModel used by the FavoritesActivity
 */
class FavoritesViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val showRoomRepository: ShowRoomRepository
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    /**
     * Get the favorite shows saved in the database
     */
    fun getFavorites() {
        viewModelScope.launch(dispatcher.IO) {
            _listShows.postValue(showRoomRepository.getAll())
        }
    }

    /**
     * Search into the favorite shows saved in the database
     */
    fun searchFavorites(term: String) {
        viewModelScope.launch(dispatcher.IO) {
            _listShows.postValue(showRoomRepository.search(term))
        }
    }

    /**
     * Delete a favorite show in the database
     */
    fun deleteFavorite(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            showRoomRepository.delete(show.id)
            _listShows.postValue(showRoomRepository.getAll())
        }
    }

}