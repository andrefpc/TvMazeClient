package com.andrefpc.tvmazeclient.ui.compose.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
import com.andrefpc.tvmazeclient.ui.compose.person_details.domain.use_case.PersonDetailsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PersonActivity
 */
class PersonDetailsViewModel(
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
}