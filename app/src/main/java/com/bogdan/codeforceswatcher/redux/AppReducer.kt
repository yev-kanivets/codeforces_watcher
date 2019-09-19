package com.bogdan.codeforceswatcher.redux

import com.bogdan.codeforceswatcher.feature.contests.redux.contestsReducer
import com.bogdan.codeforceswatcher.feature.users.redux.usersReducer
import org.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
        contests = contestsReducer(action, state),
        users = usersReducer(action, state)
    )
}
