package com.andrefpc.tvmazeclient.ui.compose.people

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
import com.andrefpc.tvmazeclient.ui.compose.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleActivity : ComponentActivity() {
    private val viewModel: PeopleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PeopleView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openPersonDetails.collectLatest {
                val intent = Intent(this@PeopleActivity, PersonDetailsActivity::class.java)
                intent.putExtra("person", it)
                startActivity(intent)
            }
        }
    }
}