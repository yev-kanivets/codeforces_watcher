package io.xorum.codeforceswatcher.features.users.redux.requests

import io.xorum.codeforceswatcher.db.DatabaseQueries
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.features.users.redux.getUsers
import io.xorum.codeforceswatcher.features.users.redux.models.UsersRequestResult
import io.xorum.codeforceswatcher.redux.Request
import io.xorum.codeforceswatcher.redux.ToastAction
import io.xorum.codeforceswatcher.redux.store
import kotlinx.coroutines.delay
import tw.geothings.rekotlin.Action

enum class Source(val isToastNeeded: Boolean) {
    USER(true), BROADCAST(false), BACKGROUND(false)
}

class UsersRequests {

    class FetchUsers(private val source: Source) : Request() {

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

        private fun getDifferenceAndUpdate(users: List<User>, updatedUsers: List<User>): List<Pair<String, Int>> {
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
                }
            }
            DatabaseQueries.Users.insert(updatedUsers)
            return difference
        }

        private fun getHandles(users: List<User>) = users.joinToString(separator = ";") { it.handle }

        data class Success(
                val users: List<User>,
                val notificationData: List<Pair<String, Int>>,
                val source: Source
        ) : Action

        data class Failure(override val message: String?) : ToastAction
    }

    class DeleteUser(val user: User) : Request() {

        override suspend fun execute() {
            DatabaseQueries.Users.delete(user.id)
        }
    }
}
