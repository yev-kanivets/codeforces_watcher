package com.bogdan.codeforceswatcher.network.models

import com.bogdan.codeforceswatcher.features.users.models.User

enum class Error { INTERNET, RESPONSE }

sealed class UsersRequestResult {
    data class Success(val users: List<User>) : UsersRequestResult()
    data class Failure(val error: Error) : UsersRequestResult()
}