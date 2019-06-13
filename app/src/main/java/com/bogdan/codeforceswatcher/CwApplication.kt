package com.bogdan.codeforceswatcher

import android.app.Application
import android.arch.persistence.room.Room
import android.widget.Toast
import com.bogdan.codeforceswatcher.model.ContestApi
import com.bogdan.codeforceswatcher.model.UserApi
import com.bogdan.codeforceswatcher.room.AppDatabase
import com.bogdan.codeforceswatcher.room.ContestDao
import com.bogdan.codeforceswatcher.room.UserDao
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

    lateinit var userApi: UserApi
    lateinit var contestApi: ContestApi

    override fun onCreate() {
        super.onCreate()

        app = this

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()
        contestDao = db.contestDao()

        retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        userApi = this.retrofit.create(UserApi::class.java)
        contestApi = this.retrofit.create(ContestApi::class.java)
    }


    fun showError(message: String = getString(R.string.no_internet_connection)) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        lateinit var app: CwApp
            private set
    }

}