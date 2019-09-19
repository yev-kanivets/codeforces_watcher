package com.bogdan.codeforceswatcher.feature.users.redux.request

import com.bogdan.codeforceswatcher.model.User

data class UsersResponse(
    val status: String,
    val result: List<User>
)