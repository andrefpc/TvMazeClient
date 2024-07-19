package com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.domain.model.ScreenState
import com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.CustomToolbar
import com.andrefpc.tvmazeclient.presentation.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.presentation.compose.widget.SearchBar
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerView

@Composable
fun FavoritesView(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Box {
        CustomToolbar(stringResource(id = R.string.title_activity_shows))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 84.dp)
        ) {
            SearchBar {
                viewModel.onSearchFavorites(it)
            }
            when (uiState) {
                ScreenState.Empty -> ErrorView()
                is ScreenState.Error -> ErrorView(error = (uiState as ScreenState.Error).error)
                ScreenState.Initial -> Unit
                ScreenState.Loading -> ShimmerView()
                ScreenState.Success -> FavoritesSuccessView()
            }
        }
    }
}