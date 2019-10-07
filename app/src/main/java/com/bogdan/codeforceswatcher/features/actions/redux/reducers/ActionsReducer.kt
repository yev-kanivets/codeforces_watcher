package com.bogdan.codeforceswatcher.features.actions.redux.reducers

import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import com.bogdan.codeforceswatcher.features.actions.redux.states.ActionsState
import com.bogdan.codeforceswatcher.redux.states.AppState
import org.rekotlin.Action

fun actionsReducer(action: Action, state: AppState): ActionsState {
    var newState = state.actions

    when (action) {
        is ActionsRequests.FetchActions.Success -> {
            newState = newState.copy(actions = action.actions, status = ActionsState.Status.IDLE)
        }
        is ActionsRequests.FetchActions -> {
            newState = newState.copy(status = ActionsState.Status.PENDING)
        }
    }

    return newState
}