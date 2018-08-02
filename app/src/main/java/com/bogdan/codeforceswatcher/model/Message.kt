package com.bogdan.codeforceswatcher.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import retrofit2.*
import retrofit2.http.*

@Entity
data class User(@PrimaryKey(autoGenerate = true) var id: Long = 0, val avatar: String, val rank: String?, val handle: String, val rating: Int?, val maxRating: Int?, val firstName: String?, val lastName: String?, var ratingChanges: List<RatingChange>)

data class UserResponse(val status: String, val result: List<User>)

data class RatingChange(val contestId: Int, val contestName: String, val handle: String, val rank: Int, val ratingUpdateTimeSeconds: Long, val oldRating: Int, val newRating: Int)

data class RatingChangeResponse(val status: String, val result: List<RatingChange>)

interface UserApi {

    @GET("user.rating")
    fun rating(@Query("handle") handle: String): Call<RatingChangeResponse>

    @GET("user.info")
    fun user(@Query("handles") handles: String): Call<UserResponse>

}