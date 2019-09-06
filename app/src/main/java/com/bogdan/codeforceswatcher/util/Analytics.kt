package com.bogdan.codeforceswatcher.util

import android.os.Bundle
import com.bogdan.codeforceswatcher.CwApp
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {

    var isEnabled: Boolean = true

    fun logAddContestToCalendarEvent(contestName: String) {
        if (isEnabled) {
            val params = Bundle()
            params.putString("contest_name", contestName)
            FirebaseAnalytics.getInstance(CwApp.app).logEvent("add_contest_to_google_calendar", params)
        }
    }

    fun logUsersListRefresh() {
        if (isEnabled) {
            FirebaseAnalytics.getInstance(CwApp.app).logEvent("users_list_refresh", Bundle())
        }
    }

    fun logContestsListRefresh() {
        if (isEnabled) {
            FirebaseAnalytics.getInstance(CwApp.app).logEvent("contests_list_refresh", Bundle())
        }
    }

    fun logUserAdded() {
        if (isEnabled) {
            FirebaseAnalytics.getInstance(CwApp.app).logEvent("user_added", Bundle())
        }
    }

}
