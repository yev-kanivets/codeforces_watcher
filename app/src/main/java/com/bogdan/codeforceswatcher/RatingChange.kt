package com.bogdan.codeforceswatcher

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class RatingChange(val ratingUpdateTimeChange: Long, val contestId: Int, val contestName: String, val handle: String, val rank: Int, val oldRating: Int, val newRating: Int)

data class RatingChangeResponse(val status: String, val result: List<RatingChange>)

interface RatingApi {

    @GET("user.rating")
    fun rating(@Query("handle") handle : String): Call<RatingChangeResponse>

}