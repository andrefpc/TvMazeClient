package com.andrefpc.tvmazeclient.ui.xml_based.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
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

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    /**
     * Get the favorite shows saved in the database
     */
    fun getFavorites() {
        viewModelScope.launch(dispatcher.IO) {
            val list = showRoomRepository.getAll()
            if(list.isEmpty()) {
                _showEmpty.postValue(true)
            }else{
                _listShows.postValue(list)
            }
        }
    }

    /**
     * Search into the favorite shows saved in the database
     */
    fun searchFavorites(term: String) {
        viewModelScope.launch(dispatcher.IO) {
            val list = showRoomRepository.search(term)
            if(list.isEmpty()) {
                _showEmpty.postValue(true)
            }else{
                _listShows.postValue(list)
            }
        }
    }

    /**
     * Delete a favorite show in the database
     */
    fun deleteFavorite(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            showRoomRepository.delete(show.id)
            val list = showRoomRepository.getAll()
            if(list.isEmpty()) {
                _showEmpty.postValue(true)
            }else{
                _listShows.postValue(list)
            }
        }
    }

}