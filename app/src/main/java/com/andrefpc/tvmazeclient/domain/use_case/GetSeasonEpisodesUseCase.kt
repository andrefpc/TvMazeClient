package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.domain.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodes
import javax.inject.Inject

class GetSeasonEpisodesUseCase @Inject constructor(
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase
) {
    @Throws(SeasonListNullException::class, SeasonListRequestException::class)
    suspend operator fun invoke(id: Int): List<SeasonEpisodes> {
        val seasons = getSeasonsUseCase(id)
        val episodes = getEpisodesUseCase(id)
        return joinedSeasonsAndEpisodes(seasons, episodes)
    }

    /**
     * Join Seasons and episodes to be used in the list
     */
    private fun joinedSeasonsAndEpisodes(
        seasons: List<Season>,
        episodes: List<Episode>
    ): List<SeasonEpisodes> {
        val seasonEpisodeList: MutableList<SeasonEpisodes> = arrayListOf()
        for (season in seasons) {
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonEpisodeList.add(SeasonEpisodes(season, episodesOfSeason))
        }

        return seasonEpisodeList
    }
}