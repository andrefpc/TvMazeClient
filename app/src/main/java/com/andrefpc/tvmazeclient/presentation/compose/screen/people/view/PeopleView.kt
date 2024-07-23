package com.andrefpc.tvmazeclient.presentation.compose.screen.people.view

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
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.compose.screen.people.PeopleViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.CustomToolbar
import com.andrefpc.tvmazeclient.presentation.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.presentation.compose.widget.SearchBar
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerType
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerView

@Composable
fun PeopleView(
    modifier: Modifier = Modifier,
    viewModel: PeopleViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPeople()
    }

    Box {
        CustomToolbar(stringResource(id = R.string.title_activity_people))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 84.dp)
        ) {
            SearchBar {
                viewModel.onSearchPeople(it)
            }
            when (uiState) {
                ScreenViewState.Empty -> ErrorView()
                is ScreenViewState.Error -> ErrorView(error = (uiState as ScreenViewState.Error).error)
                ScreenViewState.Initial -> Unit
                ScreenViewState.Loading -> ShimmerView(shimmerType = ShimmerType.PEOPLE)
                ScreenViewState.Success -> PeopleSuccessView()
            }
        }
    }
}