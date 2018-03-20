package com.bogdan.codeforceswatcher

import retrofit2.*
import retrofit2.http.*

data class User(val avatar: String, val rank: String, val handle: String, val rating: Int, val maxRating: Int)


data class UserResponse(val status: String, val result: List<User>)

interface UserApi {

    @GET("user.info?handles=BOGDAN")
    fun user(): Call<UserResponse>

}