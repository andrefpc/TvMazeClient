package com.andrefpc.tvmazeclient.core.domain.session

import com.andrefpc.tvmazeclient.core.data.Show

interface PinSession {
    /**
     * Save the pin in the shared preferences
     * @param [pin] Pin value
     */
    fun savePin(pin: String)
    /**
     * Get the pin from the shared preferences
     * @return List of [Show]
     */
    fun getPin(): String?
}