package io.xorum.codeforceswatcher.features.users.redux

import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.features.users.redux.models.Error
import io.xorum.codeforceswatcher.features.users.redux.models.UsersRequestResult
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import io.xorum.codeforceswatcher.redux.Message
import kotlinx.coroutines.delay

suspend fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean): UsersRequestResult {
    val response = CodeforcesApiClient.getUsers(handles)

    return response?.result?.let { users ->
        if (users.isEmpty()) {
            return UsersRequestResult.Failure(Error.Response())
        }

        if (isRatingUpdatesNeeded) {
            loadRatingUpdates(users)
        } else {
            UsersRequestResult.Success(users)
        }
    } ?: UsersRequestResult.Failure(response?.comment.toError() ?: Error.Internet())
}

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = CodeforcesApiClient.getRating(user.handle)
        response?.result?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: return UsersRequestResult.Failure(response?.comment.toError() ?: Error.Response())
    }
    return UsersRequestResult.Success(userList)
}

private fun String?.toError() = this?.let { Error.Response(Message.Custom(it)) }
