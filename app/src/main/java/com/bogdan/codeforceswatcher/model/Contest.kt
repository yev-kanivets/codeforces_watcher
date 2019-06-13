package com.bogdan.codeforceswatcher.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

@Entity
data class Contest(@PrimaryKey val id: Long, val name: String, @SerializedName("startTimeSeconds") val time: Long, val phase: String)

data class ContestResponse(val status: String, val result: List<Contest>)

interface ContestApi {

    @GET("contest.list")
    fun contests(): Call<ContestResponse>

}