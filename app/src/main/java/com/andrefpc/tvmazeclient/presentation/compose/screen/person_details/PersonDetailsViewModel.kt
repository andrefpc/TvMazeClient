package com.andrefpc.tvmazeclient.presentation.compose.screen.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.handler.PersonDetailsUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
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
 * ViewModel used by the PersonActivity
 */
@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val personDetailsHandler: PersonDetailsUseCaseHandler
) : ViewModel() {

    /**
     * State flow for the jetpack compose code
     */
    val _screenState = MutableStateFlow<ScreenViewState>(ScreenViewState.Initial)
    val screenState: StateFlow<ScreenViewState> get() = _screenState

    val _listShowState = MutableStateFlow<List<ShowViewState>>(emptyList())
    val listShowState: StateFlow<List<ShowViewState>> get() = _listShowState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _openShowDetails = MutableSharedFlow<ShowViewState>()
    val openShowDetails: SharedFlow<ShowViewState> = _openShowDetails

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _showError.emit(e)
        }
    }

    /**
     * Get the shows casted by a person in the server
     */
    fun getShows(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val list = personDetailsHandler.getPersonShows(id).map { ShowViewState(it) }
            _listShowState.update { list }
            _screenState.update { ScreenViewState.Success }
        }
    }

    /**
     * Open the show details screen
     */
    fun onShowClicked(show: ShowViewState) {
        viewModelScope.launch {
            _openShowDetails.emit(show)
        }
    }
}