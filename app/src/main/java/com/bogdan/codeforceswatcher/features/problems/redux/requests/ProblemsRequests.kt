package com.bogdan.codeforceswatcher.features.problems.redux.requests

import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.rekotlin.Action

class ProblemsRequests {

    class FetchProblems : Request() {

        override suspend fun execute() {
            val responseEn = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("en")
            }
            val responseRu = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("ru")
            }
            responseEn.await()?.body()?.result?.problems?.let { problemsEn ->
                responseRu.await()?.body()?.result?.problems?.let { problemsRu ->
                    store.dispatch(Success(mergeProblems(problemsEn, problemsRu)))
                } ?: store.dispatch(Failure(null))
            } ?: store.dispatch(Failure(null))
        }

        private fun mergeProblems(problemsEn: List<Problem>, problemsRu: List<Problem>): List<Problem> {
            val problems: MutableList<Problem> = mutableListOf()

            for (problemEn in problemsEn) {
                problemEn.enName = problemEn.name
                problems.add(problemEn)
            }

            for (problemRu in problemsRu) {
                problemRu.ruName = problemRu.name
                problems.find { problem ->
                    problem.contestId == problemRu.contestId && problem.index == problemRu.index
                }?.let { problem ->
                    problem.ruName = problemRu.ruName
                } ?: problems.add(problemRu)
            }

            return problems
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}