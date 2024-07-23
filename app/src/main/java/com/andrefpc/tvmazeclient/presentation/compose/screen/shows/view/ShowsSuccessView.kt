package com.andrefpc.tvmazeclient.presentation.compose.screen.shows.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsViewModel

@Composable
fun ShowsSuccessView(
    viewModel: ShowsViewModel = hiltViewModel()
) {
    val listShowState by viewModel.listShowState.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.testTag("ShowsSuccessView")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(listShowState.size) { index ->
                ShowItemView(show = listShowState[index]) {
                    viewModel.onShowClicked(it)
                }

                if (index == listShowState.size - 10) {
                    LaunchedEffect(Unit) {
                        viewModel.getNextPageShows()
                    }
                }
            }
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        FavoriteButton { viewModel.onFavoritesButtonClicked() }
        PeopleButton { viewModel.onPeopleButtonCLicked() }
    }
}

@Composable
fun FavoriteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite),
                contentDescription = "Favorites"
            )
        }
    }
}

@Composable
fun PeopleButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_people),
                contentDescription = "Favorites"
            )
        }
    }
}