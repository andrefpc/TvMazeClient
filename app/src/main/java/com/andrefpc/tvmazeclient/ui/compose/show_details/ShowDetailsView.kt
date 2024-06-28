package com.andrefpc.tvmazeclient.ui.compose.show_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.Image
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.extensions.StringExtensions.removeHtmlTags
import com.andrefpc.tvmazeclient.ui.compose.person_details.PersonDetailsSuccessView
import com.andrefpc.tvmazeclient.ui.compose.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.ui.compose.widget.CustomToolbar
import com.andrefpc.tvmazeclient.ui.compose.widget.ErrorView
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
                ScreenState.Loading -> ShimmerView()
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
