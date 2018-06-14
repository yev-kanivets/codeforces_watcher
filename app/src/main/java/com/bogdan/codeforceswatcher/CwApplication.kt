package com.bogdan.codeforceswatcher

import android.app.Application
import android.arch.persistence.room.Room

/**
 * Custom Application class.
 * Created on 14/06/18.
 *
 * @author Bogdan Evtushenko
 */

class CwApp : Application() {

    lateinit var userDao: UserDao

    companion object {
        lateinit var app: CwApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        app = this

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()
    }

}