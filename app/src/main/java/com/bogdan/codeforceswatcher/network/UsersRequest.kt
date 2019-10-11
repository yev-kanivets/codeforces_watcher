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

fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean, giveUsers: (Pair<List<User>?, Error?>) -> Unit) {
    val userCall = RestClient.getUsers(handles)
    userCall.enqueue(object : Callback<UsersResponse> {

        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
            response.body()?.users?.let { users ->
                if (isRatingUpdatesNeeded)
                    loadRatingUpdates(users, giveUsers)
                else
                    giveUsers(Pair(users, null))
            } ?: giveUsers(Pair(null, Error.RESPONSE))
        }

        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
            giveUsers(Pair(null, Error.INTERNET))
        }
    })
}

private fun loadRatingUpdates(
    userList: List<User>,
    giveUsers: (Pair<List<User>?, Error?>) -> Unit
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

        giveUsersOnMainThread(if (countTrueUsers < userList.size) {
            Pair(null, Error.RESPONSE)
        } else {
            Pair(userList, null)
        }, giveUsers)
    }.start()

}

private fun giveUsersOnMainThread(result: Pair<List<User>?, Error?>, giveUsers: (Pair<List<User>?, Error?>) -> Unit) {
    runBlocking {
        withContext(Dispatchers.Main) {
            giveUsers(result)
        }
    }
}