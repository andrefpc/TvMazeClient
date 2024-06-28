package com.andrefpc.tvmazeclient.ui.compose.person_details

import android.content.Intent
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
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.ui.compose.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.ui.compose.people.PeopleActivity
import com.andrefpc.tvmazeclient.ui.compose.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonDetailsActivity : ComponentActivity() {
    private val viewModel: PersonDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()

        val person = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("person", Person::class.java) as Person
        } else {
            intent.getSerializableExtra("person") as Person
        }

        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PersonDetailsView(
                        person = person,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openShowDetails.collectLatest {
                val intent = Intent(this@PersonDetailsActivity, ShowDetailsActivity::class.java)
                intent.putExtra("show", it)
                startActivity(intent)
            }
        }
    }
}