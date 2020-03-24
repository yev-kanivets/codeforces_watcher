package com.bogdan.codeforceswatcher.util

import com.crashlytics.android.Crashlytics
import io.xorum.codeforceswatcher.features.problems.redux.requests.CrashLogger

class AndroidCrashLogger : CrashLogger {

    override fun log(t: Throwable) {
        Crashlytics.logException(t)
    }
}