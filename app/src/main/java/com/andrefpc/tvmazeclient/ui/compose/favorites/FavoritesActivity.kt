package com.andrefpc.tvmazeclient.ui.compose.favorites

import android.content.Intent
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
import com.andrefpc.tvmazeclient.ui.compose.people.PeopleActivity
import com.andrefpc.tvmazeclient.ui.compose.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesActivity : ComponentActivity() {
    private val viewModel: FavoritesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FavoritesView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openShowDetails.collectLatest {
                val intent = Intent(this@FavoritesActivity, ShowDetailsActivity::class.java)
                intent.putExtra("show", it)
                startActivity(intent)
            }
        }
    }
}