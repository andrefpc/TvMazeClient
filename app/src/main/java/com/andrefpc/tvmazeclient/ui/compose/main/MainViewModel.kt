package com.andrefpc.tvmazeclient.ui.compose.main

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.session.PinSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by the FavoritesActivity
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val pinSession: PinSession
) : ViewModel() {

    private val canUseBiometrics = false
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    /**
     * State flow for the jetpack compose code
     */

    private val _buttonState = MutableStateFlow(R.string.create_button)
    val pinButtonState: StateFlow<Int> get() = _buttonState

    private val _showMessage = MutableSharedFlow<String>()
    val showMessage: MutableSharedFlow<String> get() = _showMessage

    private val _openShowScreen = MutableSharedFlow<Unit>()
    val openShowScreen: MutableSharedFlow<Unit> get() = _openShowScreen

    private val _showPhoneAuthentication = MutableSharedFlow<Unit>()
    val showPhoneAuthentication: MutableSharedFlow<Unit> get() = _showPhoneAuthentication

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            e.message?.let { _showMessage.emit(it) }
        }
    }

    /**
     * Check if the biometrics status is available to init it
     */
    fun verifyBiometrics(context: Context){
        val canUseBiometrics = checkBiometrics(context)
        if (canUseBiometrics) {
            initBiometrics(context)
        }
    }

    /**
     * Check if pin exists
     */
    fun checkPin() {
        if (pinSession.getPin().isNullOrEmpty()) {
            _buttonState.update { R.string.create_button }
        } else {
            _buttonState.update { R.string.login_button }
        }
    }


    /**
     * Handle biometrics click
     */
    fun onBiometricsClick() {
        viewModelScope.launch(exceptionHandler) {
            if (canUseBiometrics) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                _showPhoneAuthentication.emit(Unit)
            }
        }
    }

    /**
     * Handle the pin button click
     */
    fun onPinClick(context: Context, pinText: String){
        val pin = pinSession.getPin()
        if (pinText.isEmpty()) {
            sendMessage(context.getString(R.string.empty_pin))
        } else if (pin.isNullOrEmpty()) {
            pinSession.savePin(pinText)
            _buttonState.update {  R.string.login_button }
            openShows()
        } else {
            if (pinText == pin) {
                openShows()
            } else {
                sendMessage(context.getString(R.string.incorrect_pin))
            }
        }
    }


    /**
     * Check the biometrics status
     */
    private fun checkBiometrics(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                sendMessage(context.getString(R.string.biometric_enrolled))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                sendMessage(context.getString(R.string.no_biometric_available))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                sendMessage(context.getString(R.string.biometric_unavailable))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                sendMessage(context.getString(R.string.no_biometric_available))
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                sendMessage(context.getString(R.string.biometric_unsupported))
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                sendMessage(context.getString(R.string.cant_authenticate_with_biometrics))
                false
            }
            else -> false
        }
    }

    /**
     * Init the biometrics features
     */
    private fun initBiometrics(context: Context) {
        val executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    sendMessage(context.getString(R.string.authentication_error) + errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    sendMessage(context.getString(R.string.authentication_succeeded))
                    openShows()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    sendMessage(context.getString(R.string.authentication_failed))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.authentication))
            .setSubtitle(context.getString(R.string.login_biometric))
            .setNegativeButtonText(context.getString(R.string.login_biometric_negative))
            .build()
    }

    fun sendMessage(message: String){
        viewModelScope.launch(exceptionHandler) {
            _showMessage.emit(message)
        }
    }

    private fun openShows(){
        viewModelScope.launch(exceptionHandler) {
            _openShowScreen.emit(Unit)
        }
    }

}