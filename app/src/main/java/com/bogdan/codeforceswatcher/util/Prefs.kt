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

    fun readCounter(): String {
        val defaultPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return defaultPrefs.getString(KEY_COUNTER, "") ?: ""
    }

    fun writeCounter(counter: Int) {
        val counterString = counter.toString()
        val defaultPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val editor = defaultPrefs.edit()
        editor.putString(KEY_COUNTER, counterString)
        editor.apply()
    }

    fun readAlarm(): String {
        val defaultPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return defaultPrefs.getString(KEY_COUNTER, "") ?: ""
    }

    fun writeAlarm(alarm: String) {
        val defaultPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val editor = defaultPrefs.edit()
        editor.putString(KEY_ALARM, alarm)
        editor.apply()
    }

    companion object {

        private const val KEY_COUNTER = "key_counter"
        private const val KEY_ALARM = "key_alarm"

        @SuppressLint("StaticFieldLeak")

        fun get(): Prefs {
            return Prefs(CwApp.app)
        }
    }

}
