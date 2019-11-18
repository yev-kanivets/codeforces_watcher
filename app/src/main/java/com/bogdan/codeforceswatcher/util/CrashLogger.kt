package com.bogdan.codeforceswatcher.util

import android.os.Bundle
import com.bogdan.codeforceswatcher.CwApp
import com.google.firebase.analytics.FirebaseAnalytics

object CrashLogger {

    private val instance = FirebaseAnalytics.getInstance(CwApp.app)

    fun logThrowable(t: Throwable) {
        val params = Bundle()
        params.putString("throwable_message", t.message)
        instance.logEvent("query_error", params)
    }
}