package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.model.SeasonEpisode
import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodeStatus
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.use_case.ShowDetailsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowDetailsActivity
 */
class ShowDetailsViewModel(
    private val showDetailsUseCase: ShowDetailsUseCase
) : ViewModel() {

    private val _listSeasonEpisodes = MutableLiveData<List<SeasonEpisode>>()
    val listSeasonEpisodes: LiveData<List<SeasonEpisode>> get() = _listSeasonEpisodes

    private val _listCast = MutableLiveData<List<Cast>>()
    val listCast: LiveData<List<Cast>> get() = _listCast

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _verifyFavorite = MutableLiveData<Boolean>()
    val verifyFavorite: LiveData<Boolean> get() = _verifyFavorite

    val seasonEpisodeStatusList: MutableList<SeasonEpisodeStatus> = arrayListOf()

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
            val list = showDetailsUseCase.getCast(id)
            _listCast.postValue(list)
        }
    }

    /**
     * Get the seasons of a show from the server
     */
    fun getSeasons(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val seasons = showDetailsUseCase.getSeasons(id)
            getEpisodes(seasons, id)
        }
    }

    /**
     * Get the episodes of a show from the server
     */
    private suspend fun getEpisodes(seasons: List<Season>, id: Int) {
        val episodes = showDetailsUseCase.getEpisodes(id)
        joinSeasonsAndEpisodes(seasons, episodes)
    }

    /**
     * Join Seasons and episodes to be used in the list
     */
    private fun joinSeasonsAndEpisodes(seasons: List<Season>, episodes: List<Episode>) {
        val seasonEpisodeList: MutableList<SeasonEpisode> = arrayListOf()
        for (season in seasons) {
            seasonEpisodeList.add(SeasonEpisode(season = season))
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonEpisodeStatusList.add(SeasonEpisodeStatus(season, false, episodesOfSeason))
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }

    /**
     * Change the status of a season (Opened or not)
     */
    fun changeSeason(id: Int) {
        val index = seasonEpisodeStatusList.indexOfFirst { it.season.id == id }
        seasonEpisodeStatusList[index].opened = !seasonEpisodeStatusList[index].opened
        updateList()
    }

    /**
     * Add show to the favorites list
     */
    fun addToFavorites(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            showDetailsUseCase.switchFavorite(show)
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: Show) {
        viewModelScope.launch(exceptionHandler) {
            _verifyFavorite.postValue(showDetailsUseCase.checkFavorite(show.id))
        }
    }

    /**
     * Update the list in the activity
     */
    private fun updateList() {
        val seasonEpisodeList: MutableList<SeasonEpisode> = arrayListOf()
        for (seasonStatus in seasonEpisodeStatusList) {
            seasonStatus.season.opened = seasonStatus.opened
            seasonEpisodeList.add(SeasonEpisode(season = seasonStatus.season))
            if (seasonStatus.opened) {
                seasonEpisodeList.addAll(seasonStatus.episodes.map { SeasonEpisode(episode = it) })
            }
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }
}