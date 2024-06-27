package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.AddFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetFavoritesUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetPersonShowsUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetShowsUseCase
import com.andrefpc.tvmazeclient.core.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.ui.compose.favorites.domain.use_case.FavoritesUseCase
import com.andrefpc.tvmazeclient.ui.compose.people.domain.use_case.PeopleUseCase
import com.andrefpc.tvmazeclient.ui.compose.person_details.domain.use_case.PersonDetailsUseCase
import com.andrefpc.tvmazeclient.ui.compose.show_details.domain.use_case.ShowDetailsUseCase
import com.andrefpc.tvmazeclient.ui.compose.shows.domain_use_case.ShowsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideFavoritesUseCase(showRoomRepository: ShowRoomRepository): FavoritesUseCase = FavoritesUseCase(
        getFavorites = GetFavoritesUseCase(showRoomRepository),
        deleteFavorite = DeleteFavoriteUseCase(showRoomRepository)
    )

    @Provides
    @Singleton
    fun providePeopleUseCase(tvMazeRepository: TvMazeRepository): PeopleUseCase = PeopleUseCase(
        getPeople = GetPeopleUseCase(tvMazeRepository)
    )

    @Provides
    @Singleton
    fun providePersonDetailsUseCase(tvMazeRepository: TvMazeRepository): PersonDetailsUseCase = PersonDetailsUseCase(
        getPersonShows = GetPersonShowsUseCase(tvMazeRepository)
    )

    @Provides
    @Singleton
    fun provideShowDetailsUseCase(
        showRoomRepository: ShowRoomRepository,
        tvMazeRepository: TvMazeRepository
    ): ShowDetailsUseCase = ShowDetailsUseCase(
        getCast = GetCastUseCase(tvMazeRepository),
        getSeasonEpisodes = GetSeasonEpisodesUseCase(
            getEpisodesUseCase = GetEpisodesUseCase(tvMazeRepository),
            getSeasonsUseCase = GetSeasonsUseCase(tvMazeRepository)
        ),
        addFavorite = AddFavoriteUseCase(showRoomRepository),
        checkFavorite = CheckFavoriteUseCase(showRoomRepository),
    )

    @Provides
    @Singleton
    fun provideShowsUseCase(tvMazeRepository: TvMazeRepository): ShowsUseCase = ShowsUseCase(
        getShows = GetShowsUseCase(tvMazeRepository)
    )
}