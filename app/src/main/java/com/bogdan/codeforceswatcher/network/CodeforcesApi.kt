package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsResponse
import com.bogdan.codeforceswatcher.features.contests.redux.requests.ContestsResponse
import com.bogdan.codeforceswatcher.features.problems.redux.requests.ProblemsResponse
import com.bogdan.codeforceswatcher.features.users.redux.requests.RatingChangeResponse
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CodeforcesApi {

    @GET("user.rating")
    suspend fun getRating(@Query("handle") handle: String): Response<RatingChangeResponse>

    @GET("user.info")
    suspend fun getUsers(@Query("handles") handles: String): Response<UsersResponse>

    @GET("contest.list")
    suspend fun getContests(): Response<ContestsResponse>

    @GET("recentActions")
    suspend fun getActions(
        @Query("maxCount") maxCount: Int,
        @Query("lang") lang: String
    ): Response<ActionsResponse>

    @GET("problemset.problems")
    suspend fun getProblems(
        @Query("tags") tags: String = "implementation",
        @Query("lang") lang: String
    ): Response<ProblemsResponse>
}