package com.andrefpc.tvmazeclient.di.koin

import com.andrefpc.tvmazeclient.api.TvMazeApi
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepositoryImpl
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepositoryImpl
import com.andrefpc.tvmazeclient.core.domain.session.PinSession
import com.andrefpc.tvmazeclient.core.domain.session.PinSessionImpl
import com.andrefpc.tvmazeclient.ui.xml_based.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.ui.xml_based.people.PeopleViewModel
import com.andrefpc.tvmazeclient.ui.xml_based.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.ui.xml_based.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { createApi<TvMazeApi>(retrofit = get()) }
}

val repositoryModule = module {
    single<TvMazeRepository> {
        TvMazeRepositoryImpl(tvMazeApi = get())
    }
    single<ShowRoomRepository> {
        ShowRoomRepositoryImpl(context = get())
    }
}

val sessionModule = module {
    single<PinSession> {
        PinSessionImpl(context = get())
    }
}

val viewModelModule = module {
    viewModel {
        ShowsViewModel(
            dispatcher = get(),
            tvMazeRepository = get()
        )
    }
    viewModel {
        ShowDetailsViewModel(
            dispatcher = get(),
            tvMazeRepository = get(),
            showRoomRepository = get()
        )
    }
    viewModel {
        FavoritesViewModel(
            dispatcher = get(),
            showRoomRepository = get()
        )
    }
    viewModel {
        PeopleViewModel(
            dispatcher = get(),
            tvMazeRepository = get()
        )
    }
    viewModel {
        PersonDetailsViewModel(
            dispatcher = get(),
            tvMazeRepository = get()
        )
    }
}

val remoteModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get()) }
}

val coroutineContextProviderModule = module {
    single { CoroutineContextProvider() }
}

