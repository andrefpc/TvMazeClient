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
import com.andrefpc.tvmazeclient.core.domain.session.PinSession
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

    var canUseBiometrics = false


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

    private val _initBiometrics = MutableSharedFlow<Unit>()
    val initBiometrics: MutableSharedFlow<Unit> get() = _initBiometrics

    private val _showBiometricsAuthentication = MutableSharedFlow<Unit>()
    val showBiometricsAuthentication: MutableSharedFlow<Unit> get() = _showBiometricsAuthentication

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
        viewModelScope.launch(exceptionHandler) {
            canUseBiometrics = checkBiometrics(context)
            if (canUseBiometrics) {
                _initBiometrics.emit(Unit)
            }
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
                _showBiometricsAuthentication.emit(Unit)
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
    fun checkBiometrics(context: Context): Boolean {
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

    fun sendMessage(message: String){
        viewModelScope.launch(exceptionHandler) {
            _showMessage.emit(message)
        }
    }

    fun openShows(){
        viewModelScope.launch(exceptionHandler) {
            _openShowScreen.emit(Unit)
        }
    }

}