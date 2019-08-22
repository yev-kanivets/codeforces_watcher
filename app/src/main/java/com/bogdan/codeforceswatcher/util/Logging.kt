package com.bogdan.codeforceswatcher.util

import android.os.Bundle
import com.bogdan.codeforceswatcher.CwApp
import com.google.firebase.analytics.FirebaseAnalytics

object Logging {

    var isEnabled: Boolean = true

    fun logEvent(eventName: String, params: Bundle) {
        if (isEnabled) {
            FirebaseAnalytics.getInstance(CwApp.app)
                    .logEvent(eventName, params)
        }
    }

}
