package com.andrefpc.tvmazeclient.data.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import com.andrefpc.tvmazeclient.domain.repository.preferences.PinRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PinRepository {
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