package com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowsUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowsActivity
 */
class ShowsViewModel(
    private val showsHandler: ShowsUseCaseHandler
) : ViewModel() {
    private val _listShows = MutableLiveData<List<ShowViewState>>()
    val listShows: LiveData<List<ShowViewState>> get() = _listShows

    private val _addToListShows = MutableLiveData<List<ShowViewState>>()
    val addToListShows: LiveData<List<ShowViewState>> get() = _addToListShows

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
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val shows = showsHandler.getShows(currentPage).map { ShowViewState(it) }
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
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val shows = showsHandler.getShows(searchTerm = term).map { ShowViewState(it) }
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