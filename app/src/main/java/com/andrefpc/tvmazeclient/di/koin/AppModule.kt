package com.andrefpc.tvmazeclient.di.koin

import com.andrefpc.tvmazeclient.data.remote.TvMazeApi
import com.andrefpc.tvmazeclient.data.repository.api.PersonRepositoryImpl
import com.andrefpc.tvmazeclient.data.repository.api.ShowRepositoryImpl
import com.andrefpc.tvmazeclient.data.repository.database.ShowRoomRepositoryImpl
import com.andrefpc.tvmazeclient.data.repository.preferences.PinRepositoryImpl
import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import com.andrefpc.tvmazeclient.domain.repository.preferences.PinRepository
import com.andrefpc.tvmazeclient.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.FavoritesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetFavoritesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetPersonShowsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetShowsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.PeopleUseCase
import com.andrefpc.tvmazeclient.domain.use_case.PersonDetailsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.ShowDetailsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.ShowsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.SwitchFavoriteUseCase
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.xml_based.navigation.AppNavigationXmlImpl
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.people.PeopleViewModel
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { createApi<TvMazeApi>(retrofit = get()) }
}

val repositoryModule = module {
    single<ShowRepository> {
        ShowRepositoryImpl(tvMazeApi = get())
    }
    single<PersonRepository> {
        PersonRepositoryImpl(tvMazeApi = get())
    }
    single<ShowRoomRepository> {
        ShowRoomRepositoryImpl(context = get())
    }
    single<PinRepository> {
        PinRepositoryImpl(context = get())
    }
}

val navigationModule = module {
    single<AppNavigation> {
        AppNavigationXmlImpl()
    }
}

val useCaseModules = module {
    single { CheckFavoriteUseCase(showRoomRepository = get()) }
    single { DeleteFavoriteUseCase(showRoomRepository = get()) }
    single { GetCastUseCase(showRepository = get()) }
    single { GetEpisodesUseCase(showRepository = get()) }
    single { GetFavoritesUseCase(showRoomRepository = get()) }
    single { GetPeopleUseCase(personRepository = get()) }
    single { GetPersonShowsUseCase(personRepository = get()) }
    single { GetSeasonEpisodesUseCase(getSeasonsUseCase = get(), getEpisodesUseCase = get()) }
    single { GetSeasonsUseCase(showRepository = get()) }
    single { GetShowsUseCase(showRepository = get()) }
    single { SwitchFavoriteUseCase(showRoomRepository = get()) }
    single { FavoritesUseCase(getFavorites = get(), deleteFavorite = get()) }
    single { PeopleUseCase(getPeople = get()) }
    single { PersonDetailsUseCase(getPersonShows = get()) }
    single {
        ShowDetailsUseCase(
            getCast = get(),
            getSeasonEpisodes = get(),
            getSeasons = get(),
            getEpisodes = get(),
            switchFavorite = get(),
            checkFavorite = get()
        )
    }
    single { ShowsUseCase(getShows = get()) }
}

val viewModelModule = module {
    viewModel {
        ShowsViewModel(showsUseCase = get())
    }
    viewModel {
        ShowDetailsViewModel(showDetailsUseCase = get())
    }
    viewModel {
        FavoritesViewModel(favoritesUseCase = get())
    }
    viewModel {
        PeopleViewModel(peopleUseCase = get())
    }
    viewModel {
        PersonDetailsViewModel(personDetailsUseCase = get())
    }
}

val remoteModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get()) }
}

val coroutineContextProviderModule = module {
    single { CoroutineContextProvider() }
}

