package com.bogdan.codeforceswatcher.redux.reducers

import com.bogdan.codeforceswatcher.redux.actions.UIActions
import com.bogdan.codeforceswatcher.redux.states.AppState
import com.bogdan.codeforceswatcher.redux.states.UIState
import org.rekotlin.Action

fun uiReducer(action: Action, state: AppState): UIState {
    var newState = state.ui

    when (action) {
        is UIActions.SelectHomeTab -> newState = newState.copy(selectedHomeTab = action.homeTab)
    }

    return newState
}