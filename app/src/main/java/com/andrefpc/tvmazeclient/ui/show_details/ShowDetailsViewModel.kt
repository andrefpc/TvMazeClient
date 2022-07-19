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

class ShowDetailsViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository,
    private val showRoomRepository: ShowRoomRepository
) : ViewModel() {

    private val _listSeasonEpisodes = MutableLiveData<List<SeasonEpisode>>()
    val listSeasonEpisodes: LiveData<List<SeasonEpisode>> get() = _listSeasonEpisodes

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _verifyFavorite = MutableLiveData<Boolean>()
    val verifyFavorite: LiveData<Boolean> get() = _verifyFavorite

    val seasonStatusList: MutableList<SeasonStatus> = arrayListOf()


    fun getSeasons(id: Int) {
        _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getSeasons(id)) {
                is ApiResult.Success -> {
                    _loading.postValue(false)
                    result.result?.let {
                        getEpisodes(it, id)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    private suspend fun getEpisodes(seasons: List<Season>, id: Int) {
        when (val result = tvMazeRepository.getEpisodes(id)) {
            is ApiResult.Success -> {
                _loading.postValue(false)
                result.result?.let {
                    joinSeasonsAndEpisodes(seasons, it)
                } ?: kotlin.run {
                    _error.postValue(ApiError())
                }
            }
            is ApiResult.Error -> {
                _loading.postValue(false)
                _error.postValue(result.apiError)
            }
        }
    }

    private fun joinSeasonsAndEpisodes(seasons: List<Season>, episodes: List<Episode>) {
        val seasonEpisodeList: MutableList<SeasonEpisode> = arrayListOf()
        for (season in seasons) {
            seasonEpisodeList.add(SeasonEpisode(season = season))
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonStatusList.add(SeasonStatus(season, false, episodesOfSeason))
        }

        _listSeasonEpisodes.postValue(seasonEpisodeList)
    }

    fun changeSeason(id: Int) {
        val index = seasonStatusList.indexOfFirst { it.season.id == id }
        seasonStatusList[index].opened = !seasonStatusList[index].opened
        updateList()
    }

    fun addToFavorites(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            showRoomRepository.insert(show)
        }
    }

    fun checkFavorite(show: Show) {
        viewModelScope.launch(dispatcher.IO) {
            _verifyFavorite.postValue(showRoomRepository.isFavorite(show.id))
        }
    }

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