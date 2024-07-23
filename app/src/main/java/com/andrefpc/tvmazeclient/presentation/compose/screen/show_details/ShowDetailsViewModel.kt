package com.andrefpc.tvmazeclient.presentation.compose.screen.show_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.CastViewState
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonEpisodesViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowDetailsUseCaseHandler
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
 * ViewModel used by the ShowDetailsActivity
 */
@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val showDetailsHandler: ShowDetailsUseCaseHandler
) : ViewModel() {

    /**
     * State flow for the jetpack compose code
     */
    val _screenState = MutableStateFlow<ScreenViewState>(ScreenViewState.Initial)
    val screenState: StateFlow<ScreenViewState> get() = _screenState

    val _listSeasonEpisodesState =
        MutableStateFlow<List<SeasonEpisodesViewState>>(emptyList())
    val listSeasonEpisodesState: StateFlow<List<SeasonEpisodesViewState>> get() = _listSeasonEpisodesState

    val _listCastState = MutableStateFlow<List<CastViewState>>(emptyList())
    val listCastState: StateFlow<List<CastViewState>> get() = _listCastState

    private val _favoriteState = MutableStateFlow(false)
    val favoriteState: StateFlow<Boolean> get() = _favoriteState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _openEpisode = MutableSharedFlow<EpisodeViewState>()
    val openEpisode: SharedFlow<EpisodeViewState> = _openEpisode

    private val _openPersonDetails = MutableSharedFlow<PersonViewState>()
    val openPersonDetails: SharedFlow<PersonViewState> = _openPersonDetails

    private var seasonEpisodesList: MutableList<SeasonEpisodesViewState> = arrayListOf()

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _showError.emit(e)
        }
    }

    /**
     * Get the cast and episodes of a show from the server
     */
    fun getCastAndEpisodes(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            getCast(id)
            getSeasonsEpisodes(id)
        }
    }

    /**
     * Get the cast of a show from the server
     */
    private suspend fun getCast(id: Int) {
        val list = showDetailsHandler.getCast(id).map { CastViewState(it) }
        _listCastState.update { list }
        _screenState.update { ScreenViewState.Success }
    }

    /**
     * Get the seasons of a show from the server
     */
    private suspend fun getSeasonsEpisodes(id: Int) {
        val list = showDetailsHandler.getSeasonEpisodes(id).map { SeasonEpisodesViewState(it) }
        seasonEpisodesList = list.toMutableList()
        _listSeasonEpisodesState.update { list }
        _screenState.update { ScreenViewState.Success }
    }

    /**
     * Add show to the favorites list
     */
    private fun switchFavoriteStatus(show: ShowViewState) {
        viewModelScope.launch(exceptionHandler) {
            showDetailsHandler.switchFavorite(show.toDomain())
            _favoriteState.update { !favoriteState.value }
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: ShowViewState) {
        viewModelScope.launch(exceptionHandler) {
            _favoriteState.update { showDetailsHandler.checkFavorite(show.id) }
        }
    }

    /**
     * Change the status of a season (Opened or not)
     */
    private fun onSeasonClick(id: Int) {
        val index = seasonEpisodesList.indexOfFirst { it.season.id == id }
        seasonEpisodesList[index].opened = !seasonEpisodesList[index].opened
        _listSeasonEpisodesState.update { seasonEpisodesList }
    }

    /**
     * Open the show details screen
     */
    fun onFavoriteButtonClicked(show: ShowViewState) {
        switchFavoriteStatus(show)
    }

    /**
     * Open the show details screen
     */
    fun onEpisodeClicked(episode: EpisodeViewState) {
        viewModelScope.launch {
            _openEpisode.emit(episode)
        }
    }

    /**
     * Open the show details screen
     */
    fun onPersonClicked(person: PersonViewState) {
        viewModelScope.launch {
            _openPersonDetails.emit(person)
        }
    }
}