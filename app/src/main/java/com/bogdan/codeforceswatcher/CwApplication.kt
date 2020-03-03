package com.bogdan.codeforceswatcher

import android.app.Application
import android.content.Intent
import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import com.bogdan.codeforceswatcher.features.contests.redux.requests.ContestsRequests
import com.bogdan.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import com.bogdan.codeforceswatcher.features.users.redux.requests.Source
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersRequests
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.redux.middlewares.appMiddleware
import com.bogdan.codeforceswatcher.redux.middlewares.notificationMiddleware
import com.bogdan.codeforceswatcher.redux.middlewares.toastMiddleware
import com.bogdan.codeforceswatcher.redux.reducers.appReducer
import com.bogdan.codeforceswatcher.room.RoomController
import com.bogdan.codeforceswatcher.util.PersistenceController
import com.bogdan.codeforceswatcher.util.Prefs
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.xorum.codeforceswatcher.CWDatabase
import redux.sqlDriver
import tw.geothings.rekotlin.Store

val store = Store(
        reducer = ::appReducer,
        state = RoomController.fetchAppState(),
        middleware = listOf(
                appMiddleware, notificationMiddleware, toastMiddleware
        )
)

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        RoomController.onAppCreated()
        PersistenceController.onAppCreated()
        FirebaseAnalytics.getInstance(this)

        val prefs = Prefs.get()

        fetchData()

        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
            prefs.writeAlarm("alarm")
        }

        prefs.addLaunchCount()
        initDatabase()
    }

    private fun fetchData() {
        store.dispatch(ActionsRequests.FetchActions(false))
        store.dispatch(ContestsRequests.FetchContests(false))
        store.dispatch(UsersRequests.FetchUsers(Source.BACKGROUND))
        store.dispatch(ProblemsRequests.FetchProblems(false))
    }

    private fun startAlarm() {
        val intent = Intent(this, StartAlarm::class.java)
        sendBroadcast(intent)
    }

    private fun initDatabase() {
        sqlDriver = AndroidSqliteDriver(CWDatabase.Schema, app.applicationContext, "database")
        val database = CWDatabase(sqlDriver)
        println("Here database : ${(database.userQueries.readAll().executeAsList().firstOrNull())}")
    }

    companion object {

        lateinit var app: CwApp
            private set
    }
}
