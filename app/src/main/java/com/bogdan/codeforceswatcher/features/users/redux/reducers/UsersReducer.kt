package com.bogdan.codeforceswatcher.features.users.redux.reducers

import com.bogdan.codeforceswatcher.features.add_user.redux.requests.AddUserRequests
import com.bogdan.codeforceswatcher.features.users.redux.actions.UsersActions
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersRequests
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import com.bogdan.codeforceswatcher.redux.AppState
import org.rekotlin.Action

fun usersReducer(action: Action, state: AppState): UsersState {
    var newState = state.users

    when (action) {
        is UsersRequests.FetchUsers -> {
            newState = newState.copy(
                status = UsersState.Status.PENDING
            )
        }
        is UsersRequests.FetchUsers.Success -> {
            newState = newState.copy(
                status = UsersState.Status.IDLE,
                users = action.users
            )
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

        is UsersActions.Sort -> {
            newState = newState.copy(
                sortType = action.sortType
            )
        }
    }

    return newState
}
