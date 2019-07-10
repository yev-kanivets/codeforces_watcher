package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.bogdan.codeforceswatcher.CwApp

/**
 * Util class to encapsulate Shared Preferences handling logic.
 * Created 14/08/17.
 *
 * @author Bogdan Evtushenko
 */

class Prefs constructor(private val context: Context) {

    var ratePeriod = 5

    fun readCounter(): String {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getString(KEY_COUNTER, "") ?: ""
    }

    fun writeCounter(counter: Int) {
        val counterString = counter.toString()
        val editor = getDefaultPrefs().edit()
        editor.putString(KEY_COUNTER, counterString)
        editor.apply()
    }

    fun readAlarm(): String {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getString(KEY_COUNTER, "") ?: ""
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

        private const val KEY_COUNTER = "key_counter"
        private const val KEY_ALARM = "key_alarm"
        private const val APP_RATED = "app_rated"
        private const val LAUNCH_COUNT = "launch_count"

        @SuppressLint("StaticFieldLeak")
        private val prefs: Prefs = Prefs(CwApp.app)

        fun get(): Prefs {
            return prefs
        }

    }

}
