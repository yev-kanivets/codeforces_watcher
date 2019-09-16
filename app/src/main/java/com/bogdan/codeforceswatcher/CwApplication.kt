package com.bogdan.codeforceswatcher

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        lateinit var app: CwApp
            private set
    }
}
