package com.andrefpc.tvmazeclient.presentation.compose.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import com.andrefpc.tvmazeclient.presentation.model.handler.FavoritesUseCaseHandler
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
 * ViewModel used by the FavoritesActivity
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesHandler: FavoritesUseCaseHandler
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
     * Get the favorite shows saved in the database
     */
    fun getFavorites() = viewModelScope.launch(exceptionHandler) {
        val list = favoritesHandler.getFavorites().map { ShowViewState(it) }
        if (list.isEmpty()) {
            showEmptyView()
        } else {
            showListView(list)
        }
    }

    /**
     * Search into the favorite shows saved in the database
     */
    fun onSearchFavorites(term: String) = viewModelScope.launch(exceptionHandler) {
        val list = favoritesHandler.getFavorites(term).map { ShowViewState(it) }
        if (list.isEmpty()) {
            showEmptyView()
        } else {
            showListView(list)
        }
    }

    /**
     * Delete a favorite show in the database
     */
    fun onShowDeleted(show: ShowViewState) = viewModelScope.launch(exceptionHandler) {
        val list = favoritesHandler.deleteFavorite(show.toDomain()).map { ShowViewState(it) }
        if (list.isEmpty()) {
            showEmptyView()
        } else {
            showListView(list)
        }
    }

    /**
     * Update the screen state to show the empty view
     */
    private fun showEmptyView() {
        _screenState.update { ScreenViewState.Empty }
    }

    /**
     * Update the screen state and the list state to show the list view
     */
    private fun showListView(list: List<ShowViewState>) {
        _screenState.update { ScreenViewState.Success }
        _listShowState.update { list }
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