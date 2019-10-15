package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class Error { INTERNET, RESPONSE }

fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean, onCompleted: (Pair<List<User>?, Error?>) -> Unit) {
    val userCall = RestClient.getUsers(handles)
    userCall.enqueue(object : Callback<UsersResponse> {

        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
            response.body()?.users?.let { users ->
                if (isRatingUpdatesNeeded) {
                    loadRatingUpdates(users, onCompleted)
                } else {
                    onCompleted(Pair(users, null))
                }
            } ?: onCompleted(Pair(null, Error.RESPONSE))
        }

        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
            onCompleted(Pair(null, Error.INTERNET))
        }
    })
}

private fun loadRatingUpdates(
    userList: List<User>,
    onCompleted: (Pair<List<User>?, Error?>) -> Unit
) {
    Thread {
        var countTrueUsers = 0
        for (user in userList) {
            val response = RestClient.getRating(user.handle).execute()
            response.body()?.ratingChanges?.let { ratingChanges ->
                user.ratingChanges = ratingChanges
            } ?: break
            countTrueUsers++
        }

        returnUsersOnMainThread(if (countTrueUsers < userList.size) {
            Pair(null, Error.RESPONSE)
        } else {
            Pair(userList, null)
        }, onCompleted)
    }.start()

}

private fun returnUsersOnMainThread(result: Pair<List<User>?, Error?>, onCompleted: (Pair<List<User>?, Error?>) -> Unit) {
    runBlocking {
        withContext(Dispatchers.Main) {
            onCompleted(result)
        }
    }
}