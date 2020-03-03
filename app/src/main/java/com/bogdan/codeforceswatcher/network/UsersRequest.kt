package com.bogdan.codeforceswatcher.network

import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

suspend fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean): UsersRequestResult {
    val response = RestClient.getUsers(handles)

    return if (response == null) {
        UsersRequestResult.Failure(Error.Internet())
    } else {
        response.body()?.users?.let { users ->
            if (users.isEmpty()) {
                return UsersRequestResult.Failure(Error.Response())
            }

            if (isRatingUpdatesNeeded) {
                loadRatingUpdates(users)
            } else {
                UsersRequestResult.Success(users)
            }
        } ?: buildError(response.errorBody())
    }
}

suspend fun loadRatingUpdates(userList: List<User>): UsersRequestResult {
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = RestClient.getRating(user.handle)
        response?.body()?.ratingChanges?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: return buildError(response?.errorBody())
    }
    return UsersRequestResult.Success(userList)
}

private fun extractErrorMessage(errorBody: ResponseBody?) = errorBody?.let {
    try {
        JSONObject(errorBody.string()).getString("comment")
    } catch (e: JSONException) {
        e.printStackTrace()
        null
    }
}

private fun buildError(errorBody: ResponseBody?) = UsersRequestResult.Failure(extractErrorMessage(errorBody)
        ?.let { Error.Response(it) }
        ?: Error.Response())
