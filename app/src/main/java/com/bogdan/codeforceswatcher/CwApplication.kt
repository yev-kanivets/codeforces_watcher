package com.bogdan.codeforceswatcher

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.bogdan.codeforceswatcher.room.AppDatabase
import com.bogdan.codeforceswatcher.room.ContestDao
import com.bogdan.codeforceswatcher.room.UserDao
import com.bogdan.codeforceswatcher.util.CodeforcesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Custom Application class.
 * Created on 14/06/18.
 *
 * @author Bogdan Evtushenko
 */

class CwApp : Application() {

    lateinit var userDao: UserDao
    lateinit var contestDao: ContestDao

    private lateinit var retrofit: Retrofit

    lateinit var codeforcesApi: CodeforcesApi

    override fun onCreate() {
        super.onCreate()

        app = this

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries()
                .addMigrations(object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE Contest (id INTEGER NOT NULL, " +
                                "name TEXT NOT NULL, time INTEGER NOT NULL, phase TEXT NOT NULL, PRIMARY KEY(id))")
                    }
                }).build()

        userDao = db.userDao()
        contestDao = db.contestDao()

        retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        codeforcesApi = this.retrofit.create(CodeforcesApi::class.java)
    }

    companion object {
        lateinit var app: CwApp
            private set
    }

}