package com.bogdan.codeforceswatcher.features.problems.redux

import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import org.rekotlin.Action

class ProblemsRequests {

    class FetchProblems : Request() {

        override suspend fun execute() {
            val response = RestClient.getProblems()
            response?.body()?.result?.problems?.let { problems ->
                println("Problems : $problems")
            } ?: store.dispatch(Failure("Failure"))
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}