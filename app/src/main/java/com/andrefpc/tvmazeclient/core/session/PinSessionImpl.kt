package com.andrefpc.tvmazeclient.core.session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PinSessionImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PinSession {
    companion object {
        private const val PREFS = "prefs"
        private const val PIN = "pin"
    }

    override fun savePin(pin: String) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(PIN, pin)
        editor.apply()
    }

    override fun getPin(): String? {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return sharedPref.getString(PIN, "")
    }
}