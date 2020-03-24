package com.bogdan.codeforceswatcher.features.add_user.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.getUsers
import com.bogdan.codeforceswatcher.features.users.models.UsersRequestResult
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import io.xorum.codeforceswatcher.db.DatabaseQueries
import io.xorum.codeforceswatcher.features.users.models.User
import tw.geothings.rekotlin.Action

class AddUserRequests {

    class AddUser(val handle: String) : Request() {
        override suspend fun execute() {
            when (val result = getUsers(handle, true)) {
                is UsersRequestResult.Failure -> store.dispatch(Failure(result.error.message))
                is UsersRequestResult.Success -> result.users.firstOrNull()?.let { user -> addUser(user) }
            }
        }

        private fun addUser(user: User) {
            val foundUser = DatabaseQueries.Users.getAll()
                    .find { currentUser -> currentUser.handle == user.handle }

            if (foundUser == null) {
                user.id = DatabaseQueries.Users.insert(user)
                store.dispatch(Success(user))
            } else {
                store.dispatch(Failure(CwApp.app.getString(R.string.user_already_added)))
            }
        }

        data class Success(val user: User) : Action

        data class Failure(override val message: String) : ToastAction
    }
}
