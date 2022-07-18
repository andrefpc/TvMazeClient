package com.andrefpc.tvmazeclient.di

import com.andrefpc.tvmazeclient.api.TvMazeApi
import com.andrefpc.tvmazeclient.repositories.TvMazeRepository
import com.andrefpc.tvmazeclient.repositories.TvMazeRepositoryImpl
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { createApi<TvMazeApi>(retrofit = get()) }
}

val repositoryModule = module {
    single<TvMazeRepository> {
        TvMazeRepositoryImpl(tvMazeApi = get())
    }
}

val viewModelModule = module {
    /*viewModel {
        MainViewModel(
            dispatchers = get(),
            redditRepository = get(),
            imageUtil = get()
        )
    }*/
}

val remoteModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get()) }
}

val coroutineContextProviderModule = module {
    single { CoroutineContextProvider() }
}

