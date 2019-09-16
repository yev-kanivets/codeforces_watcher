package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.network.model.ContestResponse
import com.bogdan.codeforceswatcher.network.model.RatingChangeResponse
import com.bogdan.codeforceswatcher.network.model.UserResponse
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
