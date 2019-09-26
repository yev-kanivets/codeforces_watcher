package com.bogdan.codeforceswatcher.feature.users.redux.request

import com.bogdan.codeforceswatcher.model.User
import com.google.gson.annotations.SerializedName

data class UsersResponse(
    val status: String,
    @SerializedName("ratingChanges") val users: List<User>
)