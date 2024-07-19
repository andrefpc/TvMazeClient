package com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.FavoritesViewModel

@Composable
fun FavoritesSuccessView(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val listShowState by viewModel.listShowState.collectAsState()
    val listState = rememberLazyListState()

    Box {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(listShowState.size) { index ->
                FavoritesItemView(show = listShowState[index],
                    onClick = {
                        viewModel.onShowClicked(it)
                    }, onDelete = {
                        viewModel.onShowDeleted(it)
                    }
                )
            }
        }
    }
}