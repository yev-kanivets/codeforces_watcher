package com.bogdan.codeforceswatcher.features.users

import com.bogdan.codeforceswatcher.features.users.models.Error
import com.bogdan.codeforceswatcher.features.users.models.UsersRequestResult
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
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
    } ?: UsersRequestResult.Failure(response?.comment?.let { Error.Response(it) }
            ?: Error.Internet())
}

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = CodeforcesApiClient.getRating(user.handle)
        response?.result?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: return UsersRequestResult.Failure(response?.comment?.let { Error.Response(it) }
                ?: Error.Response())
    }
    return UsersRequestResult.Success(userList)
}
