package com.andrefpc.tvmazeclient.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val showRoomRepository: ShowRoomRepository
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    fun getShows() {
        viewModelScope.launch(dispatcher.IO) {
            _listShows.postValue(showRoomRepository.getAll())
        }
    }

    fun searchShows(term: String) {
        viewModelScope.launch(dispatcher.IO) {
            _listShows.postValue(showRoomRepository.search(term))
        }
    }

}