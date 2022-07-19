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
import kotlinx.coroutines.launch

class ShowsViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getShows(page: Int) {
        _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getShows(page)) {
                is ApiResult.Success -> {
                    _loading.postValue(false)
                    result.result?.let {
                        _listShows.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    fun searchShows(term: String) {
        _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.searchShows(term)) {
                is ApiResult.Success -> {
                    _loading.postValue(false)
                    result.result?.let {
                        _listShows.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

}