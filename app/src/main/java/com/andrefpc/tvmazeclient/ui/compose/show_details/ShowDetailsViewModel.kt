package com.andrefpc.tvmazeclient.ui.compose.show_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.SeasonEpisodeStatus
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.show_details.domain.use_case.ShowDetailsUseCase
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
    private val showDetailsUseCase: ShowDetailsUseCase
) : ViewModel() {

    /**
     * State flow for the jetpack compose code
     */
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Initial)
    val screenState: StateFlow<ScreenState> get() = _screenState

    private val _listSeasonEpisodesState = MutableStateFlow<List<SeasonEpisodeStatus>>(emptyList())
    val listSeasonEpisodesState: StateFlow<List<SeasonEpisodeStatus>> get() = _listSeasonEpisodesState

    private val _listCastState = MutableStateFlow<List<Cast>>(emptyList())
    val listCastState: StateFlow<List<Cast>> get() = _listCastState

    private val _favoriteState = MutableStateFlow(false)
    val favoriteState: StateFlow<Boolean> get() = _favoriteState

    private val _showError = MutableSharedFlow<Throwable>()
    val showError: MutableSharedFlow<Throwable> get() = _showError

    private val _openEpisode = MutableSharedFlow<Episode>()
    val openEpisode: SharedFlow<Episode> = _openEpisode

    private val _openPersonDetails = MutableSharedFlow<Person>()
    val openPersonDetails: SharedFlow<Person> = _openPersonDetails

    private var seasonEpisodeStatusList: MutableList<SeasonEpisodeStatus> = arrayListOf()

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
        val list = showDetailsUseCase.getCast(id)
        _listCastState.update { list }
        _screenState.update { ScreenState.Success }
    }

    /**
     * Get the seasons of a show from the server
     */
    private suspend fun getSeasonsEpisodes(id: Int) {
        val list = showDetailsUseCase.getSeasonEpisodes(id)
        seasonEpisodeStatusList = list.toMutableList()
        _listSeasonEpisodesState.update { list }
        _screenState.update { ScreenState.Success }
    }

    /**
     * Add show to the favorites list
     */
    private fun switchFavoriteStatus(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            showDetailsUseCase.switchFavorite(show)
            _favoriteState.update { !favoriteState.value }
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            _favoriteState.update { showDetailsUseCase.checkFavorite(show.id) }
        }
    }

    /**
     * Change the status of a season (Opened or not)
     */
    private fun onSeasonClick(id: Int) {
        val index = seasonEpisodeStatusList.indexOfFirst { it.season.id == id }
        seasonEpisodeStatusList[index].opened = !seasonEpisodeStatusList[index].opened
        _listSeasonEpisodesState.update { seasonEpisodeStatusList }
    }

    /**
     * Open the show details screen
     */
    fun onFavoriteButtonClicked(show: Show) {
        switchFavoriteStatus(show)
    }

    /**
     * Open the show details screen
     */
    fun onEpisodeClicked(episode: Episode) {
        viewModelScope.launch {
            _openEpisode.emit(episode)
        }
    }

    /**
     * Open the show details screen
     */
    fun onPersonClicked(person: Person) {
        viewModelScope.launch {
            _openPersonDetails.emit(person)
        }
    }
}