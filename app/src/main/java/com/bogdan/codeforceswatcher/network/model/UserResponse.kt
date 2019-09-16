package com.bogdan.codeforceswatcher.network.model

import com.bogdan.codeforceswatcher.model.User

data class UserResponse(
    val status: String,
    val result: List<User>
)
