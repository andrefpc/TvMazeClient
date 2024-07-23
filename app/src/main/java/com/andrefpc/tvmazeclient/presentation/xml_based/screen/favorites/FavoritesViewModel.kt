package com.andrefpc.tvmazeclient.presentation.xml_based.screen.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.handler.FavoritesUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel used by the FavoritesActivity
 */
class FavoritesViewModel(
    private val favoritesHandler: FavoritesUseCaseHandler
) : ViewModel() {
    private val _listShows = MutableLiveData<List<ShowViewState>>()
    val listShows: LiveData<List<ShowViewState>> get() = _listShows

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    /**
     * Get the favorite shows saved in the database
     */
    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = favoritesHandler.getFavorites().map { ShowViewState(it) }
            if (list.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listShows.postValue(list)
            }
        }
    }

    /**
     * Search into the favorite shows saved in the database
     */
    fun searchFavorites(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = favoritesHandler.getFavorites(term = term).map { ShowViewState(it) }
            if (list.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listShows.postValue(list)
            }
        }
    }

    /**
     * Delete a favorite show in the database
     */
    fun deleteFavorite(show: ShowViewState) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesHandler.deleteFavorite(show.toDomain())
            val list = favoritesHandler.getFavorites().map { ShowViewState(it) }
            if (list.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listShows.postValue(list)
            }
        }
    }
}