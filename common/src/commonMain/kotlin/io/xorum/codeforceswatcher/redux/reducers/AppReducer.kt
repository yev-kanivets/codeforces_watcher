package io.xorum.codeforceswatcher.redux.reducers

import io.xorum.codeforceswatcher.features.actions.redux.reducers.actionsReducer
import io.xorum.codeforceswatcher.features.add_user.redux.reducers.addUserReducer
import io.xorum.codeforceswatcher.features.users.redux.reducers.usersReducer
import io.xorum.codeforceswatcher.features.problems.redux.reducers.problemsReducer
import io.xorum.codeforceswatcher.features.contests.redux.reducers.contestsReducer
import io.xorum.codeforceswatcher.redux.states.AppState
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
            contests = contestsReducer(action, state),
            users = usersReducer(action, state),
            actions = actionsReducer(action, state),
            problems = problemsReducer(action, state),
            addUserState = addUserReducer(action, state)
    )
}
