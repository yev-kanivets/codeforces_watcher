package com.bogdan.codeforceswatcher.features.add_user.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.getUsers
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.room.DatabaseClient
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.store
import org.rekotlin.Action

class AddUserRequests {

    class AddUser(
        val handle: String
    ) : Request() {

        override suspend fun execute() {
            when (val result = getUsers(handle, true)) {
                is UsersRequestResult.Failure -> dispatchError(result.error)
                is UsersRequestResult.Success -> result.users.firstOrNull()?.let { user -> addUser(user) }
            }
        }

        private fun dispatchError(error: Error) {
            when (error) {
                Error.INTERNET ->
                    store.dispatch(
                        Failure(CwApp.app.resources.getString(R.string.no_connection))
                    )
                Error.RESPONSE ->
                    store.dispatch(
                        Failure(CwApp.app.resources.getString(R.string.failed_to_fetch_users))
                    )
            }
        }

        private fun addUser(user: User) {
            val foundUser = DatabaseClient.userDao.getAll()
                .find { currentUser -> currentUser.handle == user.handle }

            if (foundUser == null) {
                user.id = DatabaseClient.userDao.insert(user)
                store.dispatch(Success(user))
            } else
                store.dispatch(Failure(CwApp.app.getString(R.string.user_already_added)))
        }


        data class Success(val user: User) : Action

        data class Failure(override val message: String) : ToastAction
    }
}