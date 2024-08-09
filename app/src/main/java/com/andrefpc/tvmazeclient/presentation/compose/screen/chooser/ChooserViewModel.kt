package com.andrefpc.tvmazeclient.presentation.compose.screen.chooser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooserViewModel @Inject constructor() : ViewModel() {
    private val _openMain = MutableSharedFlow<Unit>()
    val openMain: SharedFlow<Unit> = _openMain

    private val _openMainXml = MutableSharedFlow<Unit>()
    val openMainXml: SharedFlow<Unit> = _openMainXml

    fun openMain() = viewModelScope.launch {
        _openMain.emit(Unit)
    }

    fun openMainXml() = viewModelScope.launch {
        _openMainXml.emit(Unit)
    }
}