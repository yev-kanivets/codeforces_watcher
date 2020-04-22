package io.xorum.codeforceswatcher.features.users.redux

import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.features.users.redux.models.Error
import io.xorum.codeforceswatcher.features.users.redux.models.UsersRequestResult
import io.xorum.codeforceswatcher.redux.Message
import io.xorum.codeforceswatcher.redux.codeforcesRepository
import kotlinx.coroutines.delay

suspend fun getUsers(handles: String, isRatingUpdatesNeeded: Boolean, lang: String): UsersRequestResult {
    val response = codeforcesRepository.getUsers(handles, lang)

    return response?.result?.let { users ->
        if (users.isEmpty()) {
            return UsersRequestResult.Failure(Error.Response())
        }

        if (isRatingUpdatesNeeded) {
            loadRatingUpdates(users, lang)
        } else {
            UsersRequestResult.Success(users)
        }
    } ?: UsersRequestResult.Failure(response?.comment.toError() ?: Error.Internet())
}

suspend fun loadRatingUpdates(userList: List<User>, lang: String): UsersRequestResult {
    for (user in userList) {
        delay(250) // Because Codeforces blocks frequent queries
        val response = codeforcesRepository.getRating(user.handle, lang)
        response?.result?.let { ratingChanges ->
            user.ratingChanges = ratingChanges
        } ?: return UsersRequestResult.Failure(response?.comment.toError() ?: Error.Response())
    }
    return UsersRequestResult.Success(userList)
}

private fun String?.toError() = this?.let { Error.Response(Message.Custom(it)) }
