package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import kotlinx.coroutines.*

fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean, onCompleted: (UsersRequestResult) -> Unit) {
    GlobalScope.launch {
        try {
            val response = RestClient.getUsers(handles)
            response.body()?.users?.let { users ->
                if (isRatingUpdatesNeeded) {
                    returnResultOnMainThread(loadRatingUpdates(users), onCompleted)
                } else {
                    returnResultOnMainThread(UsersRequestResult.Success(users), onCompleted)
                }
            }
                ?: returnResultOnMainThread(UsersRequestResult.Failure(Error.RESPONSE), onCompleted)
        } catch (t: Throwable) {
            returnResultOnMainThread(UsersRequestResult.Failure(Error.INTERNET), onCompleted)
        }
    }
}

suspend fun returnResultOnMainThread(result: UsersRequestResult, onCompleted: (UsersRequestResult) -> Unit) =
    withContext(Dispatchers.Main) {
        onCompleted(result)
    }

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    var countFetchedUsers = 0
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = try {
            RestClient.getRating(user.handle).execute()
        } catch (error: java.net.SocketTimeoutException) {
            null
        }
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