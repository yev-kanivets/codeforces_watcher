package com.bogdan.codeforceswatcher.util

import android.os.Bundle
import com.bogdan.codeforceswatcher.CwApp
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {

    var isEnabled: Boolean = true

    private val instance = FirebaseAnalytics.getInstance(CwApp.app)

    fun logAddContestToCalendarEvent(contestName: String) {
        if (isEnabled) {
            val params = Bundle()
            params.putString("contest_name", contestName)
            instance.logEvent("add_contest_to_google_calendar", params)
        }
    }

    fun logUsersListRefresh() {
        if (isEnabled) {
            instance.logEvent("users_list_refresh", Bundle())
        }
    }

    fun logContestsListRefresh() {
        if (isEnabled) {
            instance.logEvent("contests_list_refresh", Bundle())
        }
    }

    fun logUserAdded() {
        if (isEnabled) {
            instance.logEvent("user_added", Bundle())
        }
    }
}
