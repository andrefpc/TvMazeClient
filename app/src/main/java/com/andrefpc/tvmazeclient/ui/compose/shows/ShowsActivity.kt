package com.andrefpc.tvmazeclient.ui.compose.shows

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
import com.andrefpc.tvmazeclient.ui.compose.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.ui.compose.people.PeopleActivity
import com.andrefpc.tvmazeclient.ui.compose.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowsActivity : ComponentActivity() {
    private val viewModel: ShowsViewModel by viewModels()
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
                startActivity(Intent(this@ShowsActivity, FavoritesActivity::class.java))
            }
        }

        lifecycleScope.launch {
            viewModel.openPeople.collectLatest {
                startActivity(Intent(this@ShowsActivity, PeopleActivity::class.java))
            }
        }

        lifecycleScope.launch {
            viewModel.openShowDetails.collectLatest {
                val intent = Intent(this@ShowsActivity, ShowDetailsActivity::class.java)
                intent.putExtra("show", it)
                startActivity(intent)
            }
        }
    }
}