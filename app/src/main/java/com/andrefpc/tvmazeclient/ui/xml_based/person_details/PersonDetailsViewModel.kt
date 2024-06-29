package com.andrefpc.tvmazeclient.ui.xml_based.person_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ApiError
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PersonActivity
 */
class PersonDetailsViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository,
) : ViewModel() {

    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    /**
     * Get the shows casted by a person in the server
     */
    fun getShows(id: Int) {
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getPersonShows(id)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        _listShows.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }

                is ApiResult.Error -> {
                    _error.postValue(result.apiError)
                }
            }
        }
    }
}