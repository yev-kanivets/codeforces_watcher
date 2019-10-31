package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.features.users.redux.requests.RatingChangeResponse
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersResponse
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean, onCompleted: (UsersRequestResult) -> Unit) {
    val userCall = RestClient.getUsers(handles)
    userCall.enqueue(object : Callback<UsersResponse> {

        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
            response.body()?.users?.let { users ->
                if (isRatingUpdatesNeeded) {
                    loadRatingUpdates(users, onCompleted)
                } else {
                    onCompleted(UsersRequestResult.Success(users))
                }
            } ?: onCompleted(UsersRequestResult.Failure(Error.RESPONSE))
        }

        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
            onCompleted(UsersRequestResult.Failure(Error.INTERNET))
        }
    })
}

private fun loadRatingUpdates(
    userList: List<User>,
    onCompleted: (UsersRequestResult) -> Unit
) {
    Thread {
        var countFetchedUsers = 0
        for (user in userList) {
            Thread.sleep(250) // Because Codeforces blocks frequent queries
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
        returnResultOnMainThread(if (countFetchedUsers < userList.size) {
            UsersRequestResult.Failure(Error.RESPONSE)
        } else {
            UsersRequestResult.Success(userList)
        }, onCompleted)
    }.start()
}

private fun returnResultOnMainThread(result: UsersRequestResult, onCompleted: (UsersRequestResult) -> Unit) {
    runBlocking {
        withContext(Dispatchers.Main) {
            onCompleted(result)
        }
    }
}