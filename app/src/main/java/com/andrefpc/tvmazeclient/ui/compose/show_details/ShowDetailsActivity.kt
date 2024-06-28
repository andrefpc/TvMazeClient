package com.andrefpc.tvmazeclient.ui.compose.show_details

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
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.ui.compose.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.ui.compose.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowDetailsActivity : ComponentActivity() {
    private val viewModel: ShowDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()

        val show = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("show", Show::class.java) as Show
        } else {
            intent.getSerializableExtra("show") as Show
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
                val intent = Intent(this@ShowDetailsActivity, PersonDetailsActivity::class.java)
                intent.putExtra("person", it)
                startActivity(intent)
            }
        }
    }
}