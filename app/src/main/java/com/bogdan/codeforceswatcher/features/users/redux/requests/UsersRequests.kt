package com.bogdan.codeforceswatcher.features.users.redux.requests

import io.xorum.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.getUsers
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import io.xorum.codeforceswatcher.db.DatabaseQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import tw.geothings.rekotlin.Action

enum class Source(val isToastNeeded: Boolean) {
    USER(true), BROADCAST(false), BACKGROUND(false)
}

class UsersRequests {

    class FetchUsers(
            private val source: Source
    ) : Request() {

        override suspend fun execute() {
            // Use this delay because actions, problems and contests requests managed to work out(and Codeforces didn't block them)
            if (source == Source.BACKGROUND) delay(1500)
            val users = store.state.users.users
            when (val result = getUsers(getHandles(users), true)) {
                is UsersRequestResult.Failure -> {
                    store.dispatch(Failure(if (source.isToastNeeded) result.error.message else null))
                }
                is UsersRequestResult.Success -> {
                    store.dispatch(Success(result.users, getDifferenceAndUpdate(users, result.users), source))
                }
            }
        }

        private suspend fun getDifferenceAndUpdate(users: List<User>, updatedUsers: List<User>): List<Pair<String, Int>> {
            val difference: MutableList<Pair<String, Int>> = mutableListOf()
            for (user in updatedUsers) {
                users.find { it.handle == user.handle }?.let { foundUser ->
                    user.id = foundUser.id

                    if (foundUser.ratingChanges != user.ratingChanges) {
                        user.ratingChanges.lastOrNull()?.let { ratingChange ->
                            val delta = ratingChange.newRating - ratingChange.oldRating
                            difference.add(Pair(user.handle, delta))
                        }
                    }
                    withContext(Dispatchers.IO) {
                        DatabaseQueries.Users.insert(user)
                    }
                }
            }
            return difference
        }

        private fun getHandles(roomUserList: List<User>): String {
            var handles = ""
            for (element in roomUserList) {
                handles += element.handle + ";"
            }
            return handles
        }

        data class Success(
                val users: List<User>,
                val notificationData: List<Pair<String, Int>>,
                val source: Source
        ) : Action

        data class Failure(override val message: String?) : ToastAction
    }

}
