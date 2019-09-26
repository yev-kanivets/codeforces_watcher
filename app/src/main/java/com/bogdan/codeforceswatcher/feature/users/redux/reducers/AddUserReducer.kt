package com.bogdan.codeforceswatcher.feature.users.redux.reducers

import com.bogdan.codeforceswatcher.feature.users.redux.actions.ClearStateActions
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersRequests
import com.bogdan.codeforceswatcher.feature.users.states.AddUserState
import com.bogdan.codeforceswatcher.redux.AppState
import org.rekotlin.Action

fun addUserReducer(action: Action, state: AppState): AddUserState {
    var newState = state.addUserState

    when (action) {
        is UsersRequests.AddUser -> {
            newState = newState.copy(status = AddUserState.Status.PENDING)
        }

        is UsersRequests.AddUser.Success -> {
            newState = newState.copy(status = AddUserState.Status.DONE)
        }

        is UsersRequests.AddUser.Failure -> {
            newState = newState.copy(status = AddUserState.Status.IDLE)
        }

        is ClearStateActions.ClearAddUserState -> {
            newState = newState.copy(status = AddUserState.Status.IDLE)
        }
    }

    return newState
}
