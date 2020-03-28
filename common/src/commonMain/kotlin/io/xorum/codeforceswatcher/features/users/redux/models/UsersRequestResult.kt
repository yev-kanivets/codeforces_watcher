package io.xorum.codeforceswatcher.features.users.redux.models

import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.redux.localizedStrings

sealed class Error(val message: String) {
    class Internet(message: String = localizedStrings["No connection"].orEmpty()) : Error(message)
    class Response(message: String = localizedStrings["Failed to fetch user(s)! Wait or check handle(s)…"].orEmpty()) : Error(message)
}

sealed class UsersRequestResult {
    data class Success(val users: List<User>) : UsersRequestResult()
    data class Failure(val error: Error) : UsersRequestResult()
}
