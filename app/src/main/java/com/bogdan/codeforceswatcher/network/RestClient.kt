package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.BuildConfig
import com.bogdan.codeforceswatcher.util.CrashLogger
import com.google.gson.GsonBuilder
import io.xorum.codeforceswatcher.network.CodeforcesRestClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RestClient {
    private val retrofit by lazy {
        val okHttpClientBuild = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            okHttpClientBuild.addInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })
        }
        val okHttpClient = okHttpClientBuild.build()

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
        CodeforcesRestClient.getUsers(handle)
    } catch (t: Throwable) {
        CrashLogger.log(t)
        null
    }

    suspend fun getRating(handle: String) = try {
        codeforcesApi.getRating(handle)
    } catch (t: Throwable) {
        CrashLogger.log(t)
        null
    }

    suspend fun getActions(maxCount: Int = 100, lang: String) = try {
        codeforcesApi.getActions(maxCount, lang)
    } catch (t: Throwable) {
        CrashLogger.log(t)
        null
    }

    suspend fun getContests() = try {
        codeforcesApi.getContests()
    } catch (t: Throwable) {
        CrashLogger.log(t)
        null
    }

    suspend fun getProblems(lang: String) = try {
        codeforcesApi.getProblems(lang)
    } catch (t: Throwable) {
        CrashLogger.log(t)
        null
    }
}
