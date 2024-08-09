package com.andrefpc.tvmazeclient.presentation.compose.screen.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.model.handler.PeopleUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by the PeopleActivity
 */
@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val peopleHandler: PeopleUseCaseHandler
) : ViewModel() {
    /**
     * State flow for the jetpack compose code
     */
    val _screenState = MutableStateFlow<ScreenViewState>(ScreenViewState.Initial)
    val screenState: StateFlow<ScreenViewState> get() = _screenState

    val _listPeopleState = MutableStateFlow<List<PersonViewState>>(emptyList())
    val listPeopleState: StateFlow<List<PersonViewState>> get() = _listPeopleState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _openPersonDetails = MutableSharedFlow<PersonViewState>()
    val openPersonDetails: SharedFlow<PersonViewState> = _openPersonDetails

    var currentPage = 0
    var searching = false

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
        getPeople(currentPage)
    }

    /**
     * Get the people list from the server
     */
    fun getPeople(page: Int = 0) {
        currentPage = page
        searching = false
        if (currentPage == 0) {
            showLoading()
        } else {
            _isLoadingMore.update { true }
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val list = peopleHandler.getPeople(currentPage).map {PersonViewState(it) }
            if (currentPage == 0) {
                if (list.isEmpty()) {
                    showEmptyView()
                } else {
                    _listPeopleState.update { list }
                    _screenState.update { ScreenViewState.Success }
                }
            } else {
                _listPeopleState.value += list
                _isLoadingMore.update { false }
                _screenState.update { ScreenViewState.Success }
            }
        }
    }

    /**
     * Search the people list from the server
     * @param [term] to be searched
     */
    fun onSearchPeople(term: String) {
        searching = true
        showLoading()
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val list = peopleHandler.getPeople(searchTerm = term).map { PersonViewState(it) }
            if (list.isEmpty()) {
                showEmptyView()
            } else {
                _listPeopleState.update { list }
                _screenState.update { ScreenViewState.Success }
            }
        }
    }

    /**
     * Show the loading screen
     */
    private fun showLoading() {
        _screenState.update { ScreenViewState.Loading }
    }

    /**
     * Show the empty screen
     */
    private fun showEmptyView() {
        _screenState.update { ScreenViewState.Empty }
    }

    /**
     * Open the person details screen
     */
    fun onPersonClicked(person: PersonViewState) {
        viewModelScope.launch {
            _openPersonDetails.emit(person)
        }
    }

}