package com.andrefpc.tvmazeclient.ui.compose.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.favorites.domain.use_case.FavoritesUseCase
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
    private val favoritesUseCase: FavoritesUseCase
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
     * Get the favorite shows saved in the database
     */
    fun getFavorites() {
        viewModelScope.launch(exceptionHandler) {
            val list = favoritesUseCase.getFavorites()
            if(list.isEmpty()) {
                showEmptyView()
            }else{
                showListView(list)
            }
        }
    }

    /**
     * Search into the favorite shows saved in the database
     */
    fun onSearchFavorites(term: String) {
        viewModelScope.launch(exceptionHandler) {
            val list = favoritesUseCase.getFavorites(term)
            if(list.isEmpty()) {
                showEmptyView()
            }else{
                showListView(list)
            }
        }
    }

    /**
     * Delete a favorite show in the database
     */
    fun onShowDeleted(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            favoritesUseCase.deleteFavorite(show)
            val list = favoritesUseCase.getFavorites()
            if(list.isEmpty()) {
                showEmptyView()
            }else{
                showListView(list)
            }
        }
    }

    /**
     * Update the screen state to show the empty view
     */
    private fun showEmptyView() {
        _screenState.update { ScreenState.Empty }
    }

    /**
     * Update the screen state and the list state to show the list view
     */
    private fun showListView(list: List<Show>) {
        _screenState.update { ScreenState.Success }
        _listShowState.update { list }
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