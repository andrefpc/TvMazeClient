package com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.use_case.ShowsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowsActivity
 */
class ShowsViewModel(
    private val showsUseCase: ShowsUseCase
) : ViewModel() {
    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _addToListShows = MutableLiveData<List<Show>>()
    val addToListShows: LiveData<List<Show>> get() = _addToListShows

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    var currentPage = 0
    var searching = false

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _error.postValue(e)
            if (currentPage == 0) _loading.postValue(false)
        }
    }

    /**
     * Get the shows list from the server
     */
    fun getShows() {
        searching = false
        if (currentPage == 0) _loading.value = true
        viewModelScope.launch(exceptionHandler) {
            val shows = showsUseCase.getShows(currentPage)
            if (currentPage == 0) {
                if (shows.isEmpty()) {
                    _showEmpty.postValue(true)
                } else {
                    _listShows.postValue(shows)
                    delay(1000)
                    _loading.postValue(false)
                }
            } else {
                _addToListShows.postValue(shows)
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
        viewModelScope.launch(exceptionHandler) {
            val shows = showsUseCase.getShows(searchTerm = term)
            if (shows.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listShows.postValue(shows)
                delay(1000)
                _loading.postValue(false)
            }
        }
    }
}