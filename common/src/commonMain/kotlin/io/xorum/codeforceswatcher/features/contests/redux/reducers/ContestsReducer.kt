package io.xorum.codeforceswatcher.features.contests.redux.reducers

import io.xorum.codeforceswatcher.features.contests.redux.requests.ContestsRequests
import io.xorum.codeforceswatcher.features.contests.redux.states.ContestsState
import redux.states.AppState
import tw.geothings.rekotlin.Action

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
