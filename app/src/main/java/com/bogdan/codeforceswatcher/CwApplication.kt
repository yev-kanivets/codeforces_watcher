package com.bogdan.codeforceswatcher

import android.app.Application
import androidx.room.Room
import com.bogdan.codeforceswatcher.room.*
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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()

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