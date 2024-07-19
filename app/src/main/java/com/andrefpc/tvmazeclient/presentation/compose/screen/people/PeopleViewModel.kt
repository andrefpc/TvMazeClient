package com.andrefpc.tvmazeclient.presentation.compose.screen.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.model.ScreenState
import com.andrefpc.tvmazeclient.domain.use_case.PeopleUseCase
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
 * ViewModel used by the PeopleActivity
 */
@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val peopleUseCase: PeopleUseCase
) : ViewModel() {
    /**
     * State flow for the jetpack compose code
     */
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Initial)
    val screenState: StateFlow<ScreenState> get() = _screenState

    private val _listPeopleState = MutableStateFlow<List<Person>>(emptyList())
    val listPeopleState: StateFlow<List<Person>> get() = _listPeopleState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _openPersonDetails = MutableSharedFlow<Person>()
    val openPersonDetails: SharedFlow<Person> = _openPersonDetails

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
        viewModelScope.launch(exceptionHandler) {
            val list = peopleUseCase.getPeople(currentPage)
            if (currentPage == 0) {
                if (list.isEmpty()) {
                    showEmptyView()
                } else {
                    _listPeopleState.update { list }
                    _screenState.update { ScreenState.Success }
                }
            } else {
                _listPeopleState.value += list
                _isLoadingMore.update { false }
                _screenState.update { ScreenState.Success }
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
        viewModelScope.launch(exceptionHandler) {
            val list = peopleUseCase.getPeople(searchTerm = term)
            if (list.isEmpty()) {
                showEmptyView()
            } else {
                _listPeopleState.update { list }
                _screenState.update { ScreenState.Success }
            }
        }
    }

    /**
     * Show the loading screen
     */
    private fun showLoading() {
        _screenState.update { ScreenState.Loading }
    }

    /**
     * Show the empty screen
     */
    private fun showEmptyView() {
        _screenState.update { ScreenState.Empty }
    }

    /**
     * Open the person details screen
     */
    fun onPersonClicked(person: Person) {
        viewModelScope.launch {
            _openPersonDetails.emit(person)
        }
    }

}