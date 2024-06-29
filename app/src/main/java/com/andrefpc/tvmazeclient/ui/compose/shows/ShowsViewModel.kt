package com.andrefpc.tvmazeclient.ui.compose.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.shows.domain_use_case.ShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by the ShowsActivity
 */
@HiltViewModel
class ShowsViewModel @Inject constructor(
    private val showsUseCase: ShowsUseCase
) : ViewModel() {
    /**
     * State flow for the jetpack compose code
     */
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Initial)
    val screenState: StateFlow<ScreenState> get() = _screenState

    private val _listShowState = MutableStateFlow<List<Show>>(emptyList())
    val listShowState: StateFlow<List<Show>> get() = _listShowState

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _openFavorites = MutableSharedFlow<Unit>()
    val openFavorites: SharedFlow<Unit> = _openFavorites

    private val _openPeople = MutableSharedFlow<Unit>()
    val openPeople: SharedFlow<Unit> = _openPeople

    private val _openShowDetails = MutableSharedFlow<Show>()
    val openShowDetails: SharedFlow<Show> = _openShowDetails

    var currentPage = 0
    var searching = false

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _showError.emit(e)
        }
    }

    /**
     * Get the shows in the next page
     */
    fun getNextPageShows() {
        currentPage++
        getShows(currentPage)
    }

    /**
     * Get the shows list from the server
     */
    fun getShows(page: Int = 0) {
        currentPage = page
        searching = false
        if (currentPage == 0) {
            _screenState.update { ScreenState.Loading }
        } else {
            _isLoadingMore.update { true }
        }
        viewModelScope.launch(exceptionHandler) {
            val list = showsUseCase.getShows(page = currentPage)
            if (currentPage == 0) {
                if (list.isEmpty()) {
                    _screenState.update { ScreenState.Empty }
                } else {
                    _listShowState.update { list }
                    _screenState.update { ScreenState.Success }
                }
            } else {
                _isLoadingMore.update { false }
                _listShowState.value += list
                _screenState.update { ScreenState.Success }
            }
        }
    }

    /**
     * Search the shows list from the server
     * @param [term] to be searched
     */
    fun searchShows(term: String) {
        searching = true
        _screenState.update { ScreenState.Loading }
        viewModelScope.launch(exceptionHandler) {
            val list = showsUseCase.getShows(searchTerm = term)
            if (list.isEmpty()) {
                _screenState.update { ScreenState.Empty }
            } else {
                _listShowState.update { list }
                _screenState.update { ScreenState.Success }
            }
        }
    }

    /**
     * Open the favorites screen
     */
    fun onFavoritesButtonClicked() {
        viewModelScope.launch {
            _openFavorites.emit(Unit)
        }
    }

    /**
     * Open the people screen
     */
    fun onPeopleButtonCLicked() {
        viewModelScope.launch {
            _openPeople.emit(Unit)
        }
    }

    /**
     * Open the show details screen
     */
    fun onShowClicked(show: Show) {
        viewModelScope.launch {
            _openShowDetails.emit(show)
        }
    }
}