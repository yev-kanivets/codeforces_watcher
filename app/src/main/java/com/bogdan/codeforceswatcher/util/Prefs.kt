package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.bogdan.codeforceswatcher.CwApp

class Prefs constructor(private val context: Context) {

    var ratePeriod = 5

    fun readSpinnerSortPosition(): String {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getString(KEY_SPINNER_SORT_POSITION, "0") ?: "0"
    }

    fun writeSpinnerSortPosition(spinnerSortPosition: Int) {
        val editor = getDefaultPrefs().edit()
        editor.putString(KEY_SPINNER_SORT_POSITION, spinnerSortPosition.toString())
        editor.apply()
    }

    fun readAlarm(): String {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getString(KEY_SPINNER_SORT_POSITION, "") ?: ""
    }

    fun writeAlarm(alarm: String) {
        val editor = getDefaultPrefs().edit()
        editor.putString(KEY_ALARM, alarm)
        editor.apply()
    }

    fun addLaunchCount() {
        val defaultPrefs = getDefaultPrefs()
        val editor = defaultPrefs.edit()
        editor.putInt(LAUNCH_COUNT, defaultPrefs.getInt(LAUNCH_COUNT, 0) + 1)
        editor.apply()
    }

    fun checkRateDialog(): Boolean {
        val defaultPrefs = getDefaultPrefs()

        val appRated = defaultPrefs.getBoolean(APP_RATED, false)
        if (appRated) return false

        val launchCount = defaultPrefs.getInt(LAUNCH_COUNT, 0)
        return launchCount % ratePeriod == 0
    }

    fun appRated() {
        val editor = getDefaultPrefs().edit()
        editor.putBoolean(APP_RATED, true)
        editor.apply()
    }

    private fun getDefaultPrefs(): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    companion object {

        private const val KEY_SPINNER_SORT_POSITION = "key_counter"
        private const val KEY_ALARM = "key_alarm"
        private const val APP_RATED = "app_rated"
        private const val LAUNCH_COUNT = "launch_count"

        @SuppressLint("StaticFieldLeak")
        private val prefs: Prefs = Prefs(CwApp.app)

        fun get() = prefs
    }
}
