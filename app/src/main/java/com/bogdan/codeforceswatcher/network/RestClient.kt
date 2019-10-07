package com.bogdan.codeforceswatcher.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        val gsonBuilder = GsonBuilder()
            //.excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create()

        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)

        return@lazy Retrofit.Builder()
            .baseUrl("http://www.codeforces.com/api/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private val codeforcesApi by lazy { retrofit.create(CodeforcesApi::class.java) }

    fun getUsers(handle: String) = codeforcesApi.getUsers(handle)

    fun getRating(handle: String) = codeforcesApi.getRating(handle)

    fun getActions(maxCount: Int = 30) = codeforcesApi.getActions(maxCount)

    fun getContests() = codeforcesApi.getContests()
}
