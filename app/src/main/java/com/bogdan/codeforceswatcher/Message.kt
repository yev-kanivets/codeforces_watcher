package com.bogdan.codeforceswatcher

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import retrofit2.*
import retrofit2.http.*

@Entity
data class User(@PrimaryKey(autoGenerate = true) var id: Long, val avatar: String, val rank: String?, val handle: String, val rating: Int?, val maxRating: Int?, val firstName: String?, val lastName: String?)

data class UserResponse(val status: String, val result: List<User>)

interface UserApi {

    @GET("user.info")
    fun user(@Query("handles") handles: String): Call<UserResponse>

}