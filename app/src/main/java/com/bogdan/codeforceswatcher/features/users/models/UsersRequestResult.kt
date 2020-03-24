package com.bogdan.codeforceswatcher.features.users.models

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.features.users.models.User

sealed class Error(val message: String) {
    class Internet(message: String = CwApp.app.resources.getString(R.string.no_connection)) : Error(message)
    class Response(message: String = CwApp.app.resources.getString(R.string.failed_to_fetch_users)) : Error(message)
}

sealed class UsersRequestResult {
    data class Success(val users: List<User>) : UsersRequestResult()
    data class Failure(val error: Error) : UsersRequestResult()
}