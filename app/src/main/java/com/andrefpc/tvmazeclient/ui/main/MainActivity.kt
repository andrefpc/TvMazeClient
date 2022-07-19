package com.andrefpc.tvmazeclient.ui.main

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
import com.andrefpc.tvmazeclient.databinding.ActivityMainBinding
import com.andrefpc.tvmazeclient.ui.shows.ShowsActivity
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val canUseBiometrics = checkBiometrics()
        if (canUseBiometrics) {
            initBiometrics()
        }

        binding.enter.setOnClickListener {
            if (canUseBiometrics) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                showRegularLoginActivity()
            }
        }

        if (checkBiometrics()) {
            initBiometrics()
        }
    }

    private var regularLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                notifyUser("Authentication succeeded!")
                val intent = Intent(this, ShowsActivity::class.java)
                startActivity(intent)
            } else {
                notifyUser("Login cancelled by the user")
            }
        }


    /**
     * Show regular Confirmation dialog.
     */
    private fun showRegularLoginActivity() {
        val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (km.isKeyguardSecure) {
            val authIntent = km.createConfirmDeviceCredentialIntent(
                "Regular login",
                "Log in using your preferred credential"
            )
            regularLoginLauncher.launch(authIntent)
        } else {
            notifyUser("Your phone don't have any security login")
        }
    }

    private fun initBiometrics() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication succeeded!")
                    val intent = Intent(this@MainActivity, ShowsActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    notifyUser("Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }

    private fun checkBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                notifyUser("No biometric or device credential is enrolled.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                notifyUser("No biometric features available on this device.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                notifyUser("Biometric features are currently unavailable.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                notifyUser("No biometric features available on this device.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                notifyUser("Biometric is unsupported on this device.")
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                notifyUser("Tha app can't authenticate with biometrics")
                false
            }
            else -> false
        }
    }

    private fun notifyUser(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}