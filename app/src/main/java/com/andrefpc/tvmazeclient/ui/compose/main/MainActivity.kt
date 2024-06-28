package com.andrefpc.tvmazeclient.ui.compose.main

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.ui.compose.shows.ShowsActivity
import com.andrefpc.tvmazeclient.ui.compose.theme.TVMazeClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        enableEdgeToEdge()
        setContent {
            TVMazeClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.showPhoneAuthentication.collectLatest {
                openPhoneAuthentication()
            }
        }

        lifecycleScope.launch {
            viewModel.openShowScreen.collectLatest {
                openShows()
            }
        }

        lifecycleScope.launch {
            viewModel.showMessage.collectLatest {message ->
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Result launcher for the regular phone authentication
     */
    private var regularLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.sendMessage(getString(R.string.authentication_succeeded))
                openShows()
            } else {
                viewModel.sendMessage(getString(R.string.login_canceled))
            }
        }

    /**
     * Show phone Authentication
     */
    private fun openPhoneAuthentication() {
        val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (km.isKeyguardSecure) {
            val authIntent = km.createConfirmDeviceCredentialIntent(
                getString(R.string.authentication),
                getString(R.string.login_preferred_credential)
            )
            regularLoginLauncher.launch(authIntent)
        } else {
            viewModel.sendMessage(getString(R.string.phone_dont_have_sercurity_login))
        }
    }

    /**
     * Open the ShowsActivity
     */
    private fun openShows() {
        val intent = Intent(this@MainActivity, ShowsActivity::class.java)
        startActivity(intent)
    }
}