package com.bogdan.codeforceswatcher

import android.app.Application
import com.bogdan.codeforceswatcher.redux.middlewares.appMiddleware
import com.bogdan.codeforceswatcher.redux.appReducer
import com.bogdan.codeforceswatcher.redux.middlewares.errorMiddleware
import com.bogdan.codeforceswatcher.redux.middlewares.notificationMiddleware
import com.bogdan.codeforceswatcher.room.RoomController
import com.bogdan.codeforceswatcher.util.PersistenceController
import com.google.firebase.analytics.FirebaseAnalytics
import org.rekotlin.Store

val store = Store(
    reducer = ::appReducer,
    state = RoomController.fetchAppState(),
    middleware = listOf(
        appMiddleware, notificationMiddleware, errorMiddleware
    )
)

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        RoomController.onAppCreated()
        PersistenceController.onAppCreated()
        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        lateinit var app: CwApp
            private set
    }
}
