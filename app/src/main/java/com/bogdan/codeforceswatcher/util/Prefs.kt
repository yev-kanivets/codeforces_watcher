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

    private val defaultPrefs: SharedPreferences
        get() = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor
        get() = defaultPrefs.edit()

    fun readCounter(): String {
        return defaultPrefs.getString(KEY_COUNTER, "")
    }

    fun writeCounter(counter: Int) {
        val counterString = counter.toString()
        val editor = editor
        editor.putString(KEY_COUNTER, counterString)
        editor.apply()
    }

    fun readAlarm(): String {
        return defaultPrefs.getString(KEY_ALARM, "")
    }

    fun writeAlarm(alarm: String) {
        val editor = editor
        editor.putString(KEY_ALARM, alarm)
        editor.apply()
    }

    companion object {

        private const val KEY_COUNTER = "key_counter"
        private const val KEY_ALARM = "key_alarm"

        @SuppressLint("StaticFieldLeak")
        private var prefs: Prefs? = null

        fun get(): Prefs? {
            if (prefs == null) {
                prefs = Prefs(CwApp.app)
            }
            return prefs
        }
    }

}
