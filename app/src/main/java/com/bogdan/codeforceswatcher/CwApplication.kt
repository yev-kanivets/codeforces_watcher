package com.bogdan.codeforceswatcher

import android.app.Application
import android.arch.persistence.room.Room
import android.widget.Toast
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

    private lateinit var retrofit: Retrofit

    lateinit var userApi: UserApi

    override fun onCreate() {
        super.onCreate()

        app = this

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()

        retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        userApi = this.retrofit.create(UserApi::class.java)
    }


    fun showError() {
        Toast.makeText(applicationContext, getString(R.string.connectionerror), Toast.LENGTH_SHORT).show()
    }

    companion object {
        lateinit var app: CwApp
            private set
    }

}