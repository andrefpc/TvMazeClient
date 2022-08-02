package com.andrefpc.tvmazeclient.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.data.ApiError
import com.andrefpc.tvmazeclient.data.ApiResult
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.repositories.TvMazeRepository
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowsActivity
 */
class ShowsViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _addToListShows = MutableLiveData<List<Show>>()
    val addToListShows: LiveData<List<Show>> get() = _addToListShows

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    var currentPage = 0
    var searching = false

    /**
     * Get the shows list from the server
     */
    fun getShows() {
        searching = false
        if(currentPage == 0) _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getShows(currentPage)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        if(currentPage == 0) _listShows.postValue(it)
                        else _addToListShows.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                    delay(1000)
                    if(currentPage == 0) _loading.postValue(false)
                }
                is ApiResult.Error -> {
                    if(currentPage == 0) _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    /**
     * Search the shows list from the server
     * @param [term] to be searched
     */
    fun searchShows(term: String) {
        searching = true
        _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.searchShows(term)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        _listShows.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                    delay(1000)
                    _loading.postValue(false)
                }
                is ApiResult.Error -> {
                    _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }
}