package com.bogdan.codeforceswatcher.util

import com.crashlytics.android.Crashlytics

object CrashLogger {

    fun log(t: Throwable) {
        Crashlytics.logException(t)
    }

    fun problemsNotConvenience() {
        Crashlytics.log("Problems not convenience")
    }
}