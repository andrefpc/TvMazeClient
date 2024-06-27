package com.andrefpc.tvmazeclient.ui.compose.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
import com.andrefpc.tvmazeclient.ui.compose.shows.domain_use_case.ShowsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowsActivity
 */
class ShowsViewModel(
    private val showsUseCase: ShowsUseCase
) : ViewModel() {
    /**
     * State flow for the jetpack compose code
     */
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Initial)
    val screenState: StateFlow<ScreenState> get() = _screenState

    private val _listShowState = MutableStateFlow<List<Show>>(emptyList())
    val listShowState: StateFlow<List<Show>> get() = _listShowState

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
     * Get the shows list from the server
     */
    fun getShows() {
        searching = false
        if(currentPage == 0) _screenState.update { ScreenState.Loading }
        viewModelScope.launch(exceptionHandler) {
            val list = showsUseCase.getShows(page = currentPage)
            if(currentPage == 0){
                if(list.isEmpty()){
                    _screenState.update { ScreenState.Empty }
                }else{
                    _listShowState.update { list }
                }
            }
            else {
                _listShowState.value += list
            }

            _screenState.update { ScreenState.Success }
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
            if(list.isEmpty()){
                _screenState.update { ScreenState.Empty }
            }else{
                _listShowState.update { list }
            }
        }
    }
}