package com.example.controldeinventarios

import android.content.Context

class PreferencesHelper(context: Context) {
    private val fileName = "share_preferences_file"
    private val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    private val TOKEN_API = "tokenApi"

    var tokenApi: String?
        get() = prefs.getString(TOKEN_API, null)
        set(value) = prefs.edit().putString(TOKEN_API, value).apply()
}