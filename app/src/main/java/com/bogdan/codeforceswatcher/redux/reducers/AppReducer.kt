package com.bogdan.codeforceswatcher.redux.reducers

import com.bogdan.codeforceswatcher.features.actions.redux.reducers.actionsReducer
import com.bogdan.codeforceswatcher.features.add_user.redux.reducers.addUserReducer
import com.bogdan.codeforceswatcher.features.contests.redux.reducers.contestsReducer
import com.bogdan.codeforceswatcher.features.problems.redux.reducers.problemsReducer
import com.bogdan.codeforceswatcher.features.users.redux.reducers.usersReducer
import com.bogdan.codeforceswatcher.redux.states.AppState
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
            contests = contestsReducer(action, state),
            users = usersReducer(action, state),
            actions = actionsReducer(action, state),
            problems = problemsReducer(action, state),
            addUserState = addUserReducer(action, state),
            ui = uiReducer(action, state)
    )
}