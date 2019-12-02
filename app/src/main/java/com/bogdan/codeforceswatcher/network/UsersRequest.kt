package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import kotlinx.coroutines.*

suspend fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean): UsersRequestResult {
    val response = RestClient.getUsers(handles)
    return if (response == null) {
        UsersRequestResult.Failure(Error.INTERNET)
    } else {
        response.body()?.users?.let { users ->
            if (users.isEmpty()) {
                return UsersRequestResult.Failure(Error.RESPONSE)
            }

            if (isRatingUpdatesNeeded) {
                loadRatingUpdates(users)
            } else {
                UsersRequestResult.Success(users)
            }
        } ?: UsersRequestResult.Failure(Error.RESPONSE)
    }
}

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    var countFetchedUsers = 0

    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = RestClient.getRating(user.handle)
        response?.body()?.ratingChanges?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: break

        countFetchedUsers++
    }
    return if (countFetchedUsers < userList.size) {
        UsersRequestResult.Failure(Error.RESPONSE)
    } else {
        UsersRequestResult.Success(userList)
    }
}