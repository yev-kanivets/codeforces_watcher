package com.bogdan.codeforceswatcher.redux

import com.bogdan.codeforceswatcher.features.contests.redux.contestsReducer
import com.bogdan.codeforceswatcher.features.add_user.redux.reducers.addUserReducer
import com.bogdan.codeforceswatcher.features.users.redux.reducers.usersReducer
import org.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
        contests = contestsReducer(action, state),
        users = usersReducer(action, state),
        addUserState = addUserReducer(action, state)
    )
}
