package com.andrefpc.tvmazeclient.ui.compose.show_details

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
import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.ui.compose.widget.ShimmerType
import com.andrefpc.tvmazeclient.ui.compose.widget.ShimmerView
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShowDetailsView(
    show: Show,
    modifier: Modifier = Modifier,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()
    var episode: Episode? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        viewModel.getCastAndEpisodes(show.id)
        viewModel.checkFavorite(show)

        viewModel.openEpisode.collectLatest {
            episode = it
        }
    }

    Box {
        Column(modifier = modifier.fillMaxSize()) {
            when (uiState) {
                ScreenState.Empty -> ErrorView()
                is ScreenState.Error -> ErrorView(error = (uiState as ScreenState.Error).error)
                ScreenState.Initial -> Unit
                ScreenState.Loading -> ShimmerView(shimmerType = ShimmerType.DETAILS)
                ScreenState.Success -> ShowDetailsSuccessView(show)
            }
        }

        episode?.let {
            EpisodeDetailsDialog(it) {
                episode = null
            }
        }
    }
}
