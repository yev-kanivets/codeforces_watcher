package io.xorum.codeforceswatcher.features.add_user.redux.requests

import io.xorum.codeforceswatcher.features.users.redux.getUsers
import io.xorum.codeforceswatcher.features.users.redux.models.UsersRequestResult
import io.xorum.codeforceswatcher.db.DatabaseQueries
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.redux.*
import tw.geothings.rekotlin.Action

class AddUserRequests {

    class AddUser(
            private val handle: String,
            private val language: String
    ) : Request() {
        override suspend fun execute() {
            when (val result = getUsers(handle, true, lang = defineLang())) {
                is UsersRequestResult.Failure -> store.dispatch(Failure(result.error.message))
                is UsersRequestResult.Success -> result.users.firstOrNull()?.let { user -> addUser(user) }
            }
        }

        private fun defineLang(): String {
            return if (language == "ru" || language == "uk") "ru" else "en"
        }

        private fun addUser(user: User) {
            val foundUser = DatabaseQueries.Users.getAll()
                    .find { currentUser -> currentUser.handle == user.handle }

            if (foundUser == null) {
                user.id = DatabaseQueries.Users.insert(user)
                store.dispatch(Success(user))
            } else {
                store.dispatch(Failure(Message.UserAlreadyAdded))
            }
        }

        data class Success(val user: User) : Action

        data class Failure(override val message: Message) : ToastAction
    }
}
