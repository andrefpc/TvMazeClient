package com.andrefpc.tvmazeclient.presentation.compose.screen.show_details

import android.os.Build
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
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.view.ShowDetailsView
import com.andrefpc.tvmazeclient.presentation.compose.theme.TVMazeClientTheme
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowDetailsActivity : ComponentActivity() {
    private val viewModel: ShowDetailsViewModel by viewModels()

    @Inject
    lateinit var appNavigation: AppNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()

        val show = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("show", ShowViewState::class.java) as ShowViewState
        } else {
            intent.getParcelableExtra("show")
        }

        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowDetailsView(
                        show = show,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openPersonDetails.collectLatest {
                appNavigation.navigateTo(
                    this@ShowDetailsActivity,
                    NavigatorScreen.PersonDetails(it)
                )
            }
        }
    }
}