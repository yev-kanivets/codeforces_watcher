package com.bogdan.codeforceswatcher.features.users.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.Error
import com.bogdan.codeforceswatcher.network.getUsers
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.room.DatabaseClient
import com.bogdan.codeforceswatcher.store
import org.rekotlin.Action

class UsersRequests {

    class FetchUsers(
        private val isInitiatedByUser: Boolean
    ) : Request() {

        override fun execute() {
            val users: List<User> = DatabaseClient.userDao.getAll()
            getUsers(getHandles(users), true) {
                val updatedUsers = it.first
                val error = it.second
                if (error != null) {
                    when (error) {
                        Error.INTERNET ->
                            store.dispatch(
                                Failure(
                                    if (isInitiatedByUser)
                                        CwApp.app.resources.getString(R.string.no_connection)
                                    else
                                        null
                                )
                            )
                        Error.RESPONSE ->
                            store.dispatch(
                                Failure(
                                    if (isInitiatedByUser)
                                        CwApp.app.resources.getString(R.string.failed_to_fetch_users)
                                    else
                                        null
                                )
                            )
                    }
                } else {
                    if (updatedUsers != null) {
                        store.dispatch(
                            Success(updatedUsers, getDifferenceAndUpdate(users, updatedUsers), isInitiatedByUser)
                        )
                    }
                }
            }
        }

        private fun getDifferenceAndUpdate(users: List<User>, updatedUsers: List<User>): List<Pair<String, Int>> {
            val difference: MutableList<Pair<String, Int>> = mutableListOf()
            for (user in updatedUsers) {
                users.find { it.handle == user.handle }?.let { foundUser ->
                    user.id = foundUser.id

                    if (foundUser.ratingChanges != user.ratingChanges) {
                        foundUser.ratingChanges.lastOrNull()?.let { ratingChange ->
                            val delta = ratingChange.newRating - ratingChange.oldRating
                            difference.add(Pair(user.handle, delta))
                        }
                    }
                    DatabaseClient.userDao.update(user)
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
            val notificationData: List<Pair<String, Int>>, val isUserInitiated: Boolean
        ) : Action

        data class Failure(override val message: String?) : ToastAction
    }

}