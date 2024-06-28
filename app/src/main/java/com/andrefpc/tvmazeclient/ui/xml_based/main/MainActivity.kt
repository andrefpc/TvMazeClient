package com.andrefpc.tvmazeclient.ui.xml_based.main

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.ActivityMainBinding
import com.andrefpc.tvmazeclient.core.session.PinSession
import com.andrefpc.tvmazeclient.ui.xml_based.shows.ShowsActivity
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

/**
 * Main screen of the application
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val pinSession: PinSession by inject()

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (pinSession.getPin().isNullOrEmpty()) {
            binding.enter.text = getString(R.string.create_button)
        } else {
            binding.enter.text = getString(R.string.login_button)
        }

        val canUseBiometrics = checkBiometrics()
        if (canUseBiometrics) {
            initBiometrics()
        }

        initListeners(canUseBiometrics)
    }

    /**
     * Init the listeners of the screen
     */
    private fun initListeners(canUseBiometrics: Boolean) {
        binding.phoneSecurity.setOnClickListener {
            if (canUseBiometrics) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                openPhoneAuthentication()
            }
        }

        binding.enter.setOnClickListener {
            val pin = pinSession.getPin()
            if (pin.isNullOrEmpty()) {
                pinSession.savePin(binding.pin.text.toString())
                binding.enter.text = getString(R.string.login_button)
                openShows()
            } else {
                if (binding.pin.text.toString() == pin) {
                    openShows()
                } else {
                    notifyUser(getString(R.string.incorrect_pin))
                }
            }
        }
    }

    /**
     * Result launcher for the regular phone authentication
     */
    private var regularLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                notifyUser(getString(R.string.authentication_succeeded))
                openShows()
            } else {
                notifyUser(getString(R.string.login_canceled))
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
            notifyUser(getString(R.string.phone_dont_have_sercurity_login))
        }
    }

    /**
     * Init the biometrics features
     */
    private fun initBiometrics() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser(getString(R.string.authentication_error) + errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser(getString(R.string.authentication_succeeded))
                    openShows()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    notifyUser(getString(R.string.authentication_failed))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.authentication))
            .setSubtitle(getString(R.string.login_biometric))
            .setNegativeButtonText(getString(R.string.login_biometric_negative))
            .build()
    }

    /**
     * Open the ShowsActivity
     */
    private fun openShows() {
        val intent = Intent(this@MainActivity, ShowsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Check the biometrics status
     */
    private fun checkBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                notifyUser(getString(R.string.biometric_enrolled))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                notifyUser(getString(R.string.no_biometric_available))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                notifyUser(getString(R.string.biometric_unavailable))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                notifyUser(getString(R.string.no_biometric_available))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                notifyUser(getString(R.string.biometric_unsupported))
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                notifyUser(getString(R.string.cant_authenticate_with_biometrics))
                false
            }
            else -> false
        }
    }

    /**
     * Send toasts messages to the user
     */
    private fun notifyUser(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}