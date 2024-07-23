package com.andrefpc.tvmazeclient.presentation.compose.screen.shows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.view.ShowsView
import com.andrefpc.tvmazeclient.presentation.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowsActivity : ComponentActivity() {
    val viewModel: ShowsViewModel by viewModels()

    @Inject
    lateinit var appNavigation: AppNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowsView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openFavorites.collectLatest {
                appNavigation.navigateTo(this@ShowsActivity, NavigatorScreen.Favorites)
            }
        }

        lifecycleScope.launch {
            viewModel.openPeople.collectLatest {
                appNavigation.navigateTo(this@ShowsActivity, NavigatorScreen.People)
            }
        }

        lifecycleScope.launch {
            viewModel.openShowDetails.collectLatest {
                appNavigation.navigateTo(this@ShowsActivity, NavigatorScreen.ShowDetails(it))
            }
        }
    }
}