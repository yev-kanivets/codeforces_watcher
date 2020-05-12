package com.bogdan.codeforceswatcher

import android.app.Application
import android.content.Intent
import com.bogdan.codeforceswatcher.handlers.AndroidMessageHandler
import com.bogdan.codeforceswatcher.handlers.AndroidNotificationHandler
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.util.FeedbackController
import com.bogdan.codeforceswatcher.util.Prefs
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.xorum.codeforceswatcher.CWDatabase
import io.xorum.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import io.xorum.codeforceswatcher.features.contests.redux.requests.ContestsRequests
import io.xorum.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import io.xorum.codeforceswatcher.features.users.redux.requests.Source
import io.xorum.codeforceswatcher.features.users.redux.requests.UsersRequests
import io.xorum.codeforceswatcher.redux.*
import io.xorum.codeforceswatcher.redux.middlewares.notificationHandler
import io.xorum.codeforceswatcher.redux.middlewares.toastHandlers
import io.xorum.codeforceswatcher.util.settings
import java.util.*

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        initDatabase()
        initSettings()
        initToastHandler()
        initNotificationHandler()

        databaseController.onAppCreated()
        persistenceController.onAppCreated()

        FirebaseAnalytics.getInstance(this)

        fetchData()

        if (Prefs.get().readAlarm().isEmpty()) {
            startAlarm()
            Prefs.get().writeAlarm()
        }

        FeedbackController.get().updateCountOpeningScreen()
    }

    private fun initDatabase() {
        sqlDriver = AndroidSqliteDriver(CWDatabase.Schema, app.applicationContext, "database")
    }

    private fun initSettings() {
        settings = Prefs.get()
    }

    private fun initToastHandler() {
        toastHandlers.add(AndroidMessageHandler())
    }

    private fun initNotificationHandler() {
        notificationHandler = AndroidNotificationHandler()
    }

    private fun fetchData() {
        store.dispatch(ActionsRequests.FetchActions(false, Locale.getDefault().language))
        store.dispatch(ContestsRequests.FetchContests(false))
        store.dispatch(UsersRequests.FetchUsers(Source.BACKGROUND, Locale.getDefault().language))
        store.dispatch(ProblemsRequests.FetchProblems(false))
        store.dispatch(ActionsRequests.FetchPinnedPost())
    }

    private fun startAlarm() {
        val intent = Intent(this, StartAlarm::class.java)
        sendBroadcast(intent)
    }

    companion object {
        lateinit var app: CwApp
            private set
    }
}
