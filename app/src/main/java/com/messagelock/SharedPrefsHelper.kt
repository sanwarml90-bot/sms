package com.messagelock

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("MessageLockPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TRUSTED_NUMBER = "trusted_number"
    }

    fun saveTrustedNumber(number: String) {
        prefs.edit().putString(KEY_TRUSTED_NUMBER, number.trim()).apply()
    }

    fun getTrustedNumber(): String? {
        return prefs.getString(KEY_TRUSTED_NUMBER, null)
    }
}
