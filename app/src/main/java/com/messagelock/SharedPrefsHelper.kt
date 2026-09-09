package com.messagelock

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("MessageLockPrefs", Context.MODE_PRIVATE)
    fun setSirenActive(active: Boolean) = prefs.edit().putBoolean(KEY_SIREN_ACTIVE, active).apply()
    fun isSirenActive(): Boolean = prefs.getBoolean(KEY_SIREN_ACTIVE, false)
    companion object { private const val KEY_SIREN_ACTIVE = "siren_active" }
}
