package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.network.CodeforcesRestClient
import kotlinx.coroutines.delay

suspend fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean): UsersRequestResult {
    val response = CodeforcesRestClient.getUsers(handles)

    return response?.result?.let { users ->
        if (users.isEmpty()) {
            return UsersRequestResult.Failure(Error.Response())
        }

        if (isRatingUpdatesNeeded) {
            loadRatingUpdates(users)
        } else {
            UsersRequestResult.Success(users)
        }
    } ?: UsersRequestResult.Failure(response?.comment?.let { Error.Response(it) }
            ?: Error.Internet())
}

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = CodeforcesRestClient.getRating(user.handle)
        response?.result?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: return UsersRequestResult.Failure(response?.comment?.let { Error.Response(it) }
                ?: Error.Response())
    }
    return UsersRequestResult.Success(userList)
}
