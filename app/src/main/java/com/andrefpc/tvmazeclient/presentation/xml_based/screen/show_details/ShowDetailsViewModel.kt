package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowDetailsUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.CastViewState
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonEpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonEpisodesViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowDetailsActivity
 */
class ShowDetailsViewModel(
    private val showDetailsHandler: ShowDetailsUseCaseHandler
) : ViewModel() {

    private val _listSeasonEpisodes = MutableLiveData<List<SeasonEpisodeViewState>>()
    val listSeasonEpisodes: LiveData<List<SeasonEpisodeViewState>> get() = _listSeasonEpisodes

    private val _listCast = MutableLiveData<List<CastViewState>>()
    val listCast: LiveData<List<CastViewState>> get() = _listCast

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _verifyFavorite = MutableLiveData<Boolean>()
    val verifyFavorite: LiveData<Boolean> get() = _verifyFavorite

    val seasonEpisodesList: MutableList<SeasonEpisodesViewState> = arrayListOf()

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _error.postValue(e)
        }
    }

    /**
     * Get the cast of a show from the server
     */
    fun getCast(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val list = showDetailsHandler.getCast(id).map { CastViewState(it) }
            _listCast.postValue(list)
        }
    }

    /**
     * Get the seasons of a show from the server
     */
    fun getSeasons(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val seasons = showDetailsHandler.getSeasons(id)
            getEpisodes(
                seasons.map { SeasonViewState(it) },
                id
            )
        }
    }

    /**
     * Get the episodes of a show from the server
     */
    private suspend fun getEpisodes(seasons: List<SeasonViewState>, id: Int) {
        val episodes = showDetailsHandler.getEpisodes(id)
        joinSeasonsAndEpisodes(
            seasons,
            episodes.map { EpisodeViewState(it) }
        )
    }

    /**
     * Join Seasons and episodes to be used in the list
     */
    private fun joinSeasonsAndEpisodes(
        seasons: List<SeasonViewState>,
        episodes: List<EpisodeViewState>
    ) {
        val seasonEpisodeList: MutableList<SeasonEpisodeViewState> = arrayListOf()
        for (season in seasons) {
            seasonEpisodeList.add(SeasonEpisodeViewState(season = season))
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonEpisodesList.add(
                SeasonEpisodesViewState(
                    season = season,
                    episodes = episodesOfSeason
                )
            )
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }

    /**
     * Change the status of a season (Opened or not)
     */
    fun changeSeason(id: Int) {
        val index = seasonEpisodesList.indexOfFirst { it.season.id == id }
        seasonEpisodesList[index].opened = !seasonEpisodesList[index].opened
        updateList()
    }

    /**
     * Add show to the favorites list
     */
    fun addToFavorites(show: ShowViewState) {
        viewModelScope.launch(exceptionHandler) {
            showDetailsHandler.switchFavorite(show.toDomain())
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: ShowViewState) {
        viewModelScope.launch(exceptionHandler) {
            _verifyFavorite.postValue(showDetailsHandler.checkFavorite(show.id))
        }
    }

    /**
     * Update the list in the activity
     */
    private fun updateList() {
        val seasonEpisodeList: MutableList<SeasonEpisodeViewState> = arrayListOf()
        for (seasonStatus in seasonEpisodesList) {
            seasonStatus.season.opened = seasonStatus.opened
            seasonEpisodeList.add(SeasonEpisodeViewState(season = seasonStatus.season))
            if (seasonStatus.opened) {
                seasonEpisodeList.addAll(seasonStatus.episodes.map { SeasonEpisodeViewState(episode = it) })
            }
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }
}