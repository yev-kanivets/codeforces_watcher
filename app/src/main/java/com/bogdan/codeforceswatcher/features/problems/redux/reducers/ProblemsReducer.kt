package com.bogdan.codeforceswatcher.features.problems.redux.reducers

import com.bogdan.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import com.bogdan.codeforceswatcher.features.problems.redux.states.ProblemsState
import com.bogdan.codeforceswatcher.redux.states.AppState
import org.rekotlin.Action

fun problemsReducer(action: Action, state: AppState): ProblemsState {
    var newState = state.problems

    when (action) {
        is ProblemsRequests.FetchProblems.Success -> {
            newState = newState.copy(problems = action.problems)
        }
    }

    return newState
}