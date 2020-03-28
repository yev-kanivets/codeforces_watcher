package com.bogdan.codeforceswatcher

import android.app.Application
import android.content.Intent
import io.xorum.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import io.xorum.codeforceswatcher.features.contests.redux.requests.ContestsRequests
import io.xorum.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.handlers.AndroidMessageHandler
import com.bogdan.codeforceswatcher.handlers.AndroidNotificationHandler
import com.bogdan.codeforceswatcher.util.PersistenceController
import com.bogdan.codeforceswatcher.util.Prefs
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.xorum.codeforceswatcher.CWDatabase
import io.xorum.codeforceswatcher.db.DatabaseController
import io.xorum.codeforceswatcher.db.settings
import io.xorum.codeforceswatcher.features.users.redux.requests.Source
import io.xorum.codeforceswatcher.features.users.redux.requests.UsersRequests
import io.xorum.codeforceswatcher.redux.localizedStrings
import io.xorum.codeforceswatcher.redux.middlewares.notificationHandler
import io.xorum.codeforceswatcher.redux.middlewares.toastHandler
import io.xorum.codeforceswatcher.redux.sqlDriver
import io.xorum.codeforceswatcher.redux.store
import java.util.*

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        initCommonModuleComponents()
        initDatabase()
        initSettings()

        DatabaseController.onAppCreated()
        PersistenceController.onAppCreated()
        FirebaseAnalytics.getInstance(this)

        fetchData()

        if (Prefs.get().readAlarm().isEmpty()) {
            startAlarm()
            Prefs.get().writeAlarm()
        }

        Prefs.get().addLaunchCount()
    }

    private fun initCommonModuleComponents() {
        toastHandler = AndroidMessageHandler()
        notificationHandler = AndroidNotificationHandler()

        localizedStrings["No connection"] = getString(R.string.no_connection)
        localizedStrings["Failed to fetch user(s)! Wait or check handle(s)…"] = getString(R.string.failed_to_fetch_users)
        localizedStrings["User already added"] = getString(R.string.user_already_added)
    }

    private fun fetchData() {
        store.dispatch(ActionsRequests.FetchActions(false, Locale.getDefault().language))
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
    }

    private fun initSettings() {
        settings = Prefs.get()
    }

    companion object {
        lateinit var app: CwApp
            private set
    }
}
