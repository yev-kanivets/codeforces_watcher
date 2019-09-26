package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.feature.contests.redux.request.ContestsResponse
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersResponse
import com.bogdan.codeforceswatcher.feature.users.redux.request.RatingChangeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CodeforcesApi {

    @GET("user.rating")
    fun getRating(@Query("handle") handle: String): Call<RatingChangeResponse>

    @GET("user.info")
    fun getUsers(@Query("handles") handles: String): Call<UsersResponse>

    @GET("contest.list")
    fun getContests(): Call<ContestsResponse>
}
