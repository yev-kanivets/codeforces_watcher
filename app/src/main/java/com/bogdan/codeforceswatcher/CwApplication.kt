package com.bogdan.codeforceswatcher

import android.app.Application
import androidx.room.Room
import com.bogdan.codeforceswatcher.room.AppDatabase
import com.bogdan.codeforceswatcher.room.ContestDao
import com.bogdan.codeforceswatcher.room.MIGRATION_1_2
import com.bogdan.codeforceswatcher.room.MIGRATION_2_3
import com.bogdan.codeforceswatcher.room.UserDao
import com.google.firebase.analytics.FirebaseAnalytics

class CwApp : Application() {

    lateinit var userDao: UserDao
    lateinit var contestDao: ContestDao

    override fun onCreate() {
        super.onCreate()

        app = this

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()

        userDao = db.userDao()
        contestDao = db.contestDao()

        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        lateinit var app: CwApp
            private set
    }
}
