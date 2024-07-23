package com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerType
import com.andrefpc.tvmazeclient.presentation.compose.widget.ShimmerView
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState

@Composable
fun PersonDetailsView(
    person: PersonViewState?,
    modifier: Modifier = Modifier,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        person?.id?.let { viewModel.getShows(it) }
    }

    Box {
        Column(modifier = modifier.fillMaxSize()) {
            if(person == null) {
                ErrorView()
            }else {
                when (uiState) {
                    ScreenViewState.Empty -> ErrorView()
                    is ScreenViewState.Error -> ErrorView(error = (uiState as ScreenViewState.Error).error)
                    ScreenViewState.Initial -> Unit
                    ScreenViewState.Loading -> ShimmerView(shimmerType = ShimmerType.DETAILS)
                    ScreenViewState.Success -> PersonDetailsSuccessView(person)
                }
            }
        }
    }
}