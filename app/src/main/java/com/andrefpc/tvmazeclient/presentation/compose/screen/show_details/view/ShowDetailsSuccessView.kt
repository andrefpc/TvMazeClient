package com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.util.extensions.StringExtensions.removeHtmlTags
import com.andrefpc.tvmazeclient.presentation.compose.theme.Gray
import com.andrefpc.tvmazeclient.presentation.compose.theme.Teal700

private const val s = "Not Started"

@Composable
fun ShowDetailsSuccessView(
    show: Show,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val listCastState by viewModel.listCastState.collectAsState()
    val listSeasonEpisodesState by viewModel.listSeasonEpisodesState.collectAsState()
    val favoriteState by viewModel.favoriteState.collectAsState()
    val lazyColumnState = rememberLazyListState()
    val lazyRowState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = show.image?.original),
                        contentDescription = show.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = show.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(20.dp)
                                .weight(1f)
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite Button",
                            tint = if (favoriteState) Teal700 else Gray,
                            modifier = Modifier
                                .padding(20.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    viewModel.onFavoriteButtonClicked(show)
                                }
                        )
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.weight(0.5f)) {
                        Text(
                            text = stringResource(id = R.string.premier_label),
                            fontSize = 16.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = show.premiered ?: stringResource(R.string.not_started),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.premier_label),
                            fontSize = 16.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = show.ended ?: stringResource(R.string.running),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = show.genres.joinToString(),
                    fontSize = 16.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = show.schedule.days.joinToString(),
                    fontSize = 16.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = show.schedule.time,
                    fontSize = 16.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = show.summary.removeHtmlTags(),
                    fontSize = 16.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.all_episodes_label),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(listSeasonEpisodesState.size) { index ->
            ShowDetailsSeasonView(seasonEpisodeStatus = listSeasonEpisodesState[index]) {
                viewModel.onEpisodeClicked(it)
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.cast),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }

        item {
            LazyRow(
                state = lazyRowState,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
            ) {
                items(listCastState.size) { index ->
                    ShowDetailsCastView(cast = listCastState[index]) {
                        viewModel.onPersonClicked(it)
                    }
                }
            }
        }
    }
}