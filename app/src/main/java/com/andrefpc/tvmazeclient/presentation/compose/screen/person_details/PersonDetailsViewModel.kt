package com.andrefpc.tvmazeclient.presentation.compose.screen.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.ScreenState
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.use_case.PersonDetailsUseCase
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
    private val personDetailsUseCase: PersonDetailsUseCase
) : ViewModel() {

    /**
     * State flow for the jetpack compose code
     */
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Initial)
    val screenState: StateFlow<ScreenState> get() = _screenState

    private val _listShowState = MutableStateFlow<List<Show>>(emptyList())
    val listShowState: StateFlow<List<Show>> get() = _listShowState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _openShowDetails = MutableSharedFlow<Show>()
    val openShowDetails: SharedFlow<Show> = _openShowDetails

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
            val list = personDetailsUseCase.getPersonShows(id)
            _listShowState.update { list }
            _screenState.update { ScreenState.Success }
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