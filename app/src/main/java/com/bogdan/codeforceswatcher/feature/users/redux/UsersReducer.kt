package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.feature.users.redux.actions.SortActions
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersRequests
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
        is SortActions.Sort -> {
            newState = newState.copy(
                sortType = action.sortType
            )
        }
    }

    return newState
}
