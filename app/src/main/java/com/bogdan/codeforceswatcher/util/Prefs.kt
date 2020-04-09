package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.bogdan.codeforceswatcher.CwApp
import io.xorum.codeforceswatcher.features.contests.models.Platform
import io.xorum.codeforceswatcher.util.Settings

class Prefs(private val context: Context) : Settings {

    private val ratePeriod = 5

    override fun readSpinnerSortPosition(): Int {
        val defaultPrefs = getDefaultPrefs()
        return (defaultPrefs.getString(KEY_SPINNER_SORT_POSITION, "0") ?: "0").toInt()
    }

    override fun writeSpinnerSortPosition(spinnerSortPosition: Int) {
        val editor = getDefaultPrefs().edit()
        editor.putString(KEY_SPINNER_SORT_POSITION, spinnerSortPosition.toString())
        editor.apply()
    }

    override fun readProblemsIsFavourite(): Boolean {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getBoolean(KEY_PROBLEMS_IS_FAVOURITE, false)
    }

    override fun writeProblemsIsFavourite(isFavourite: Boolean) {
        val editor = getDefaultPrefs().edit()
        editor.putBoolean(KEY_PROBLEMS_IS_FAVOURITE, isFavourite)
        editor.apply()
    }

    override fun readContestsFilters(): Set<String> {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getStringSet(KEY_CONTESTS_FILTERS, Platform.defaultFilterValueToSave).orEmpty()
    }

    override fun writeContestsFilters(filters: Set<String>) {
        val editor = getDefaultPrefs().edit()
        editor.putStringSet(KEY_CONTESTS_FILTERS, filters)
        editor.apply()
    }

    fun readAlarm(): String {
        val defaultPrefs = getDefaultPrefs()
        return defaultPrefs.getString(KEY_SPINNER_SORT_POSITION, "") ?: ""
    }

    fun writeAlarm() {
        val editor = getDefaultPrefs().edit()
        editor.putString(KEY_ALARM, "alarm")
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
        private const val KEY_PROBLEMS_IS_FAVOURITE = "key_problems_is_favourite"
        private const val KEY_ALARM = "key_alarm"
        private const val APP_RATED = "app_rated"
        private const val LAUNCH_COUNT = "launch_count"
        private const val KEY_CONTESTS_FILTERS = "key_contests_filters"

        @SuppressLint("StaticFieldLeak")
        private val prefs: Prefs = Prefs(CwApp.app)

        fun get() = prefs
    }
}
