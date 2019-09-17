package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.redux.AppState
import org.rekotlin.Action

fun contestsReducer(action: Action, state: AppState): ContestsState {
    var newState = state.contests

    when (action) {
        is ContestsRequests.FetchContests -> {
            newState = newState.copy(
                status = ContestsState.Status.PENDING
            )
        }
        is ContestsRequests.FetchContests.Success -> {
            newState = newState.copy(
                status = ContestsState.Status.IDLE,
                contests = action.contests
            )
        }
        is ContestsRequests.FetchContests.Failure -> {
            newState = newState.copy(
                status = ContestsState.Status.IDLE
            )
        }
    }

    return newState
}
