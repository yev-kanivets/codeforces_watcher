package io.xorum.codeforceswatcher.features.users.redux.reducers

import io.xorum.codeforceswatcher.features.add_user.redux.requests.AddUserRequests
import io.xorum.codeforceswatcher.features.users.redux.actions.UsersActions
import io.xorum.codeforceswatcher.features.users.redux.requests.UsersRequests
import io.xorum.codeforceswatcher.features.users.redux.states.UsersState
import redux.states.AppState
import tw.geothings.rekotlin.Action

fun usersReducer(action: Action, state: AppState): UsersState {
    var newState = state.users
    when (action) {
        is UsersRequests.FetchUsers -> {
            newState = newState.copy(
                    status = UsersState.Status.PENDING
            )
        }

        is UsersRequests.FetchUsers.Success -> {
            val mapUsers = action.users.associateBy { it.id }
            val newUsers = state.users.users.map { mapUsers[it.id] ?: it }
            newState = newState.copy(status = UsersState.Status.IDLE, users = newUsers)
        }

        is UsersRequests.FetchUsers.Failure -> {
            newState = newState.copy(
                    status = UsersState.Status.IDLE
            )
        }

        is AddUserRequests.AddUser.Success -> {
            newState = newState.copy(
                    users = state.users.users.plus(action.user)
            )
        }

        is UsersActions.DeleteUser -> {
            newState = newState.copy(
                    users = state.users.users.minus(action.user)
            )
        }

        is UsersActions.Sort -> {
            newState = newState.copy(
                    sortType = action.sortType
            )
        }
    }

    return newState
}
