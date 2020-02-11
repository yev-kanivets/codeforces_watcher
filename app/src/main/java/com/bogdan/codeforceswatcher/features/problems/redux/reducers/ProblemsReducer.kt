package com.bogdan.codeforceswatcher.features.problems.redux.reducers

import com.bogdan.codeforceswatcher.features.problems.redux.actions.ProblemsActions
import com.bogdan.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import com.bogdan.codeforceswatcher.features.problems.redux.states.ProblemsState
import com.bogdan.codeforceswatcher.redux.states.AppState
import org.rekotlin.Action

fun problemsReducer(action: Action, state: AppState): ProblemsState {
    var newState = state.problems

    when (action) {
        is ProblemsRequests.FetchProblems -> {
            newState = newState.copy(status = ProblemsState.Status.PENDING)
        }

        is ProblemsRequests.FetchProblems.Success -> {
            newState = newState.copy(
                    problems = action.problems,
                    status = ProblemsState.Status.IDLE
            )
        }

        is ProblemsRequests.FetchProblems.Failure -> {
            newState = newState.copy(status = ProblemsState.Status.IDLE)
        }

        is ProblemsActions.ChangeTypeProblems -> {
            newState = newState.copy(isFavourite = action.isFavourite)
        }

        is ProblemsRequests.ChangeStatusFavourite.Success -> {
            newState = newState.copy(problems = newState.problems.map {
                if (it.contestId == action.problem.contestId && it.index == action.problem.index) action.problem else it
            })
        }
    }

    return newState
}
