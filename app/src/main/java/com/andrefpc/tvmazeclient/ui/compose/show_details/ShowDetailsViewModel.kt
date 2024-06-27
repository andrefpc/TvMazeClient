package com.andrefpc.tvmazeclient.ui.compose.show_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Season
import com.andrefpc.tvmazeclient.core.data.SeasonEpisode
import com.andrefpc.tvmazeclient.core.data.SeasonEpisodeStatus
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.show_details.domain.use_case.ShowDetailsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowDetailsActivity
 */
class ShowDetailsViewModel(
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

    private var seasonEpisodeStatusList: MutableList<SeasonEpisodeStatus> = arrayListOf()

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
    * Get the cast of a show from the server
    */
    fun getCast(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val list = showDetailsUseCase.getCast(id)
            _listCastState.update { list }
            _screenState.update { ScreenState.Success }
        }
    }

    /**
     * Get the seasons of a show from the server
     */
    fun getSeasonsEpisodes(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val list = showDetailsUseCase.getSeasonEpisodes(id)
            seasonEpisodeStatusList = list.toMutableList()
            _listSeasonEpisodesState.update { list }
            _screenState.update { ScreenState.Success }
        }
    }

    /**
     * Add show to the favorites list
     */
    fun addToFavorites(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            showDetailsUseCase.addFavorite(show)
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            _favoriteState.update {  showDetailsUseCase.checkFavorite(show.id) }
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
}