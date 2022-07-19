package com.andrefpc.tvmazeclient.session

import com.andrefpc.tvmazeclient.data.Show

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