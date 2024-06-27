package com.andrefpc.tvmazeclient.ui.compose.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.ui.compose.people.domain.use_case.PeopleUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PeopleActivity
 */
class PeopleViewModel(
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
    * Get the people list from the server
    */
    fun getPeople() {
        searching = false
        if (currentPage == 0) showLoading()
        viewModelScope.launch(exceptionHandler) {
            val list = peopleUseCase.getPeople(currentPage)
            if(currentPage == 0){
                if(list.isEmpty()){
                    showEmptyView()
                }else{
                    _listPeopleState.update { list }
                }
            }
            else {
                _listPeopleState.value += list
            }
            _screenState.update { ScreenState.Success }
        }
    }

    /**
     * Search the people list from the server
     * @param [term] to be searched
     */
    fun searchPeople(term: String) {
        searching = true
        showLoading()
        viewModelScope.launch(exceptionHandler) {
            val list = peopleUseCase.getPeople(searchTerm = term)
            if(list.isEmpty()){
                showEmptyView()
            }else{
                _listPeopleState.update { list }
            }
        }
    }

    private fun showLoading() {
        _screenState.update { ScreenState.Loading }
    }

    private fun showEmptyView() {
        _screenState.update { ScreenState.Empty }
    }

}