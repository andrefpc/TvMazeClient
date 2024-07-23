package com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerType
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerView
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShowDetailsView(
    show: ShowViewState?,
    modifier: Modifier = Modifier,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()
    var episode: EpisodeViewState? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        show?.let {
            viewModel.getCastAndEpisodes(it.id)
            viewModel.checkFavorite(it)
        }

        viewModel.openEpisode.collectLatest {
            episode = it
        }
    }

    Box {
        Column(modifier = modifier.fillMaxSize()) {
            if (show == null) {
                ErrorView()
            } else {
                when (uiState) {
                    ScreenViewState.Empty -> ErrorView()
                    is ScreenViewState.Error -> ErrorView(error = (uiState as ScreenViewState.Error).error)
                    ScreenViewState.Initial -> Unit
                    ScreenViewState.Loading -> ShimmerView(shimmerType = ShimmerType.DETAILS)
                    ScreenViewState.Success -> ShowDetailsSuccessView(show)
                }
            }
        }

        episode?.let {
            EpisodeDetailsDialog(it) {
                episode = null
            }
        }
    }
}
