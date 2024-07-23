package com.andrefpc.tvmazeclient.presentation.compose.screen.chooser

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
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.view.ChooserView
import com.andrefpc.tvmazeclient.presentation.compose.theme.TVMazeClientTheme
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChooserActivity : ComponentActivity() {
    val viewModel: ChooserViewModel by viewModels()

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChooserView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.openMain.collectLatest {
                appNavigation.navigateTo(this@ChooserActivity, NavigatorScreen.Main)
            }
        }

        lifecycleScope.launch {
            viewModel.openMainXml.collectLatest {
                startActivity(Intent(this@ChooserActivity, MainActivity::class.java))
            }
        }
    }
}