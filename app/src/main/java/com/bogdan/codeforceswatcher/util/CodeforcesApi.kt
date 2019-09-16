package com.bogdan.codeforceswatcher.util

import com.bogdan.codeforceswatcher.model.ContestResponse
import com.bogdan.codeforceswatcher.model.RatingChangeResponse
import com.bogdan.codeforceswatcher.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CodeforcesApi {

    @GET("user.rating")
    fun getRating(@Query("handle") handle: String): Call<RatingChangeResponse>

    @GET("user.info")
    fun getUsers(@Query("handles") handles: String): Call<UserResponse>

    @GET("contest.list")
    fun getContests(): Call<ContestResponse>
}
