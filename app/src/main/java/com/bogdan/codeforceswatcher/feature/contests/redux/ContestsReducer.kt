package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.redux.AppState
import org.rekotlin.Action

fun contestsReducer(action: Action, state: AppState): ContestsState {
    var newState = state.contests

    when (action) {
        is ContestsRequests.FetchContests.Success -> {
            newState = newState.copy(
                contests = action.contests
            )
        }
        is ContestsRequests.FetchContests.Failure -> {
            // TODO: Handle failure
        }
    }

    return newState
}
