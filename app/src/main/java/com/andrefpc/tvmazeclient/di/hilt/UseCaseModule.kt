package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import com.andrefpc.tvmazeclient.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.presentation.model.handler.FavoritesUseCaseHandler
import com.andrefpc.tvmazeclient.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetFavoritesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetPersonShowsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetShowsUseCase
import com.andrefpc.tvmazeclient.presentation.model.handler.PeopleUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.handler.PersonDetailsUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowDetailsUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowsUseCaseHandler
import com.andrefpc.tvmazeclient.domain.use_case.SwitchFavoriteUseCase
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
    fun provideFavoritesUseCase(showRoomRepository: ShowRoomRepository): FavoritesUseCaseHandler =
        FavoritesUseCaseHandler(
            getFavorites = GetFavoritesUseCase(showRoomRepository),
            deleteFavorite = DeleteFavoriteUseCase(showRoomRepository)
        )

    @Provides
    @Singleton
    fun providePeopleUseCase(personRepository: PersonRepository): PeopleUseCaseHandler = PeopleUseCaseHandler(
        getPeople = GetPeopleUseCase(personRepository)
    )

    @Provides
    @Singleton
    fun providePersonDetailsUseCase(personRepository: PersonRepository): PersonDetailsUseCaseHandler =
        PersonDetailsUseCaseHandler(
            getPersonShows = GetPersonShowsUseCase(personRepository)
        )

    @Provides
    @Singleton
    fun provideShowDetailsUseCase(
        showRoomRepository: ShowRoomRepository,
        showRepository: ShowRepository
    ): ShowDetailsUseCaseHandler = ShowDetailsUseCaseHandler(
        getCast = GetCastUseCase(showRepository),
        getSeasonEpisodes = GetSeasonEpisodesUseCase(
            getEpisodesUseCase = GetEpisodesUseCase(showRepository),
            getSeasonsUseCase = GetSeasonsUseCase(showRepository)
        ),
        getSeasons = GetSeasonsUseCase(showRepository),
        getEpisodes = GetEpisodesUseCase(showRepository),
        switchFavorite = SwitchFavoriteUseCase(showRoomRepository),
        checkFavorite = CheckFavoriteUseCase(showRoomRepository),
    )

    @Provides
    @Singleton
    fun provideShowsUseCase(showRepository: ShowRepository): ShowsUseCaseHandler = ShowsUseCaseHandler(
        getShows = GetShowsUseCase(showRepository)
    )
}