package com.bogdan.codeforceswatcher.features.problems.redux.reducers

import com.bogdan.codeforceswatcher.features.problems.models.Problem
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

        is ProblemsRequests.MarkProblemFavorite.Success -> {
            val newProblems = mutableListOf<Problem>()
            for (problem in newState.problems) {
                if (problem.contestId == action.problem.contestId && problem.name == action.problem.name) {
                    newProblems.add(action.problem)
                } else {
                    newProblems.add(problem)
                }
            }
            newState = newState.copy(problems = newProblems)
        }
    }

    return newState
}