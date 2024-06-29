package com.andrefpc.tvmazeclient.ui.compose.person_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.ui.compose.widget.ErrorView
import com.andrefpc.tvmazeclient.ui.compose.widget.ShimmerType
import com.andrefpc.tvmazeclient.ui.compose.widget.ShimmerView

@Composable
fun PersonDetailsView(
    person: Person,
    modifier: Modifier = Modifier,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getShows(person.id)
    }

    Box {
        Column(modifier = modifier.fillMaxSize()) {
            when (uiState) {
                ScreenState.Empty -> ErrorView()
                is ScreenState.Error -> ErrorView(error = (uiState as ScreenState.Error).error)
                ScreenState.Initial -> Unit
                ScreenState.Loading -> ShimmerView(shimmerType = ShimmerType.DETAILS)
                ScreenState.Success -> PersonDetailsSuccessView(person)
            }
        }
    }
}