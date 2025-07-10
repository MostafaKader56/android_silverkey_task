package com.silverkey.task.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.silverkey.task.SilverKeyTaskApplication

object SharedPreferencesModule {
    private const val SHARED_PREFERENCES_NAME = "com.silverkey.task_SHARED_PREFERENCES_NAME"
    private val sharedPreferences: SharedPreferences =
        SilverKeyTaskApplication.instance.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    const val IS_ONBOARDING_SHOWN = "IS_ONBOARDING_SHOWN"
    const val IS_DARK_MODE_ENABLED = "IS_DARK_MODE_ENABLED"

    fun setStringValue(key: String, value: String?) {
        sharedPreferences.edit()?.apply {
            putString(key, value)
            apply()
        }
    }

    fun getStringValue(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setIntegerValue(key: String, value: Int) {
        sharedPreferences.edit()?.apply {
            putInt(key, value)
            apply()
        }
    }

    fun getIntegerValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun setBooleanValue(key: String, value: Boolean) {
        sharedPreferences.edit()?.apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}
