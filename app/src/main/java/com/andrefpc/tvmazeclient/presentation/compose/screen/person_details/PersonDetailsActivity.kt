package com.andrefpc.tvmazeclient.presentation.compose.screen.person_details

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
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.view.PersonDetailsView
import com.andrefpc.tvmazeclient.presentation.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsActivity : ComponentActivity() {
    private val viewModel: PersonDetailsViewModel by viewModels()

    @Inject
    lateinit var appNavigation: AppNavigation
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
                appNavigation.navigateTo(
                    this@PersonDetailsActivity,
                    NavigatorScreen.ShowDetails(it)
                )
            }
        }
    }
}