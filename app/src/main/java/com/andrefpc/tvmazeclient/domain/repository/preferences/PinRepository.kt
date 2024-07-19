package com.andrefpc.tvmazeclient.domain.repository.preferences

import com.andrefpc.tvmazeclient.domain.model.Show

interface PinRepository {
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