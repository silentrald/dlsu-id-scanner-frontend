package com.example.id_scanner

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "id_scanner"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun remove(key: String) {
        sharedPref.edit().remove(key).apply()
    }
}