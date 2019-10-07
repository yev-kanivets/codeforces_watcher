package com.bogdan.codeforceswatcher.features.contests.redux.reducers

import com.bogdan.codeforceswatcher.features.contests.redux.requests.ContestsRequests
import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.contests.models.Contest
import com.bogdan.codeforceswatcher.redux.states.AppState
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
                contests = action.contests.filter { it.phase == "BEFORE" }.sortedBy(Contest::time)
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
