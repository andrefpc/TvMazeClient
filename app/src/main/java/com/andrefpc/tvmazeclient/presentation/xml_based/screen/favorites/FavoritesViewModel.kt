package com.andrefpc.tvmazeclient.presentation.xml_based.screen.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.use_case.FavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel used by the FavoritesActivity
 */
class FavoritesViewModel(
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    /**
     * Get the favorite shows saved in the database
     */
    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = favoritesUseCase.getFavorites()
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
            val list = favoritesUseCase.getFavorites(term = term)
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
    fun deleteFavorite(show: Show) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.deleteFavorite(show)
            val list = favoritesUseCase.getFavorites()
            if (list.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listShows.postValue(list)
            }
        }
    }
}