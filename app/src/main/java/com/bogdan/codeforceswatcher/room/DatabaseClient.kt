package com.bogdan.codeforceswatcher.room

import androidx.room.Room
import com.bogdan.codeforceswatcher.CwApp

object DatabaseClient {

    private val database by lazy {
        Room.databaseBuilder(CwApp.app, AppDatabase::class.java, "database")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    val userDao by lazy { database.userDao() }
    val contestDao by lazy { database.contestDao() }
}
