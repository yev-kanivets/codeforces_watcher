package com.bogdan.codeforceswatcher.features.users.redux.requests

import com.bogdan.codeforceswatcher.model.User
import com.google.gson.annotations.SerializedName

data class UsersResponse(
    val status: String,
    @SerializedName("result") val users: List<User>
)