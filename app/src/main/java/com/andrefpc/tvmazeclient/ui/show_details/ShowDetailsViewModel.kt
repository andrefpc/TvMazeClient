package com.andrefpc.tvmazeclient.ui.show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.data.*
import com.andrefpc.tvmazeclient.repositories.TvMazeRepository
import com.andrefpc.tvmazeclient.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.launch

/**
 * ViewModel used by the ShowDetailsActivity
 */
class ShowDetailsViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository,
    private val showRoomRepository: ShowRoomRepository
) : ViewModel() {

    private val _listSeasonEpisodes = MutableLiveData<List<SeasonEpisode>>()
    val listSeasonEpisodes: LiveData<List<SeasonEpisode>> get() = _listSeasonEpisodes

    private val _listCast = MutableLiveData<List<Cast>>()
    val listCast: LiveData<List<Cast>> get() = _listCast

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    private val _verifyFavorite = MutableLiveData<Boolean>()
    val verifyFavorite: LiveData<Boolean> get() = _verifyFavorite

    val seasonStatusList: MutableList<SeasonStatus> = arrayListOf()

    /**
    * Get the cast of a show from the server
    */
    fun getCast(id: Int) {
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getCast(id)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        _listCast.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    /**
     * Get the seasons of a show from the server
     */
    fun getSeasons(id: Int) {
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getSeasons(id)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        getEpisodes(it, id)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    /**
     * Get the episodes of a show from the server
     */
    private suspend fun getEpisodes(seasons: List<Season>, id: Int) {
        when (val result = tvMazeRepository.getEpisodes(id)) {
            is ApiResult.Success -> {
                result.result?.let {
                    joinSeasonsAndEpisodes(seasons, it)
                } ?: kotlin.run {
                    _error.postValue(ApiError())
                }
            }
            is ApiResult.Error -> {
                _error.postValue(result.apiError)
            }
        }
    }

    /**
     * Join Seasons and episodes to be used in the list
     */
    private fun joinSeasonsAndEpisodes(seasons: List<Season>, episodes: List<Episode>) {
        val seasonEpisodeList: MutableList<SeasonEpisode> = arrayListOf()
        for (season in seasons) {
            seasonEpisodeList.add(SeasonEpisode(season = season))
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonStatusList.add(SeasonStatus(season, false, episodesOfSeason))
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }

    /**
     * Change the status of a season (Opened or not)
     */
    fun changeSeason(id: Int) {
        val index = seasonStatusList.indexOfFirst { it.season.id == id }
        seasonStatusList[index].opened = !seasonStatusList[index].opened
        updateList()
    }

    /**
     * Add show to the favorites list
     */
    fun addToFavorites(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            showRoomRepository.insert(show)
        }
    }

    /**
     * Check if the show is in the favorites list
     */
    fun checkFavorite(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            _verifyFavorite.postValue(showRoomRepository.isFavorite(show.id))
        }
    }

    /**
     * Update the list in the activity
     */
    private fun updateList() {
        val seasonEpisodeList: MutableList<SeasonEpisode> = arrayListOf()
        for (seasonStatus in seasonStatusList) {
            seasonStatus.season.opened = seasonStatus.opened
            seasonEpisodeList.add(SeasonEpisode(season = seasonStatus.season))
            if (seasonStatus.opened) {
                seasonEpisodeList.addAll(seasonStatus.episodes.map { SeasonEpisode(episode = it) })
            }
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }
}