package com.bogdan.codeforceswatcher.redux.reducers

import com.bogdan.codeforceswatcher.features.contests.redux.contestsReducer
import com.bogdan.codeforceswatcher.features.add_user.redux.reducers.addUserReducer
import com.bogdan.codeforceswatcher.features.users.redux.reducers.usersReducer
import com.bogdan.codeforceswatcher.redux.states.AppState
import org.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
        contests = contestsReducer(action, state),
        users = usersReducer(action, state),
        addUserState = addUserReducer(action, state),
        ui = uiReducer(action, state)
    )
}
