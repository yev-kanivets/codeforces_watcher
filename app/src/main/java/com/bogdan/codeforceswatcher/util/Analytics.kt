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

}
