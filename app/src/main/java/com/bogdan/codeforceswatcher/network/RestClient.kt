package com.bogdan.codeforceswatcher.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val gsonBuilder = GsonBuilder()
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

    suspend fun getUsers(handle: String) = try {
        codeforcesApi.getUsers(handle)
    } catch (t: Throwable) {
        null
    }

    suspend fun getRating(handle: String) = try {
        codeforcesApi.getRating(handle)
    } catch (t: Throwable) {
        null
    }

    suspend fun getActions(maxCount: Int = 100, lang: String) = try {
        codeforcesApi.getActions(maxCount, lang)
    } catch (t: Throwable) {
        null
    }

    suspend fun getContests() = try {
        codeforcesApi.getContests()
    } catch (t: Throwable) {
        null
    }
}
