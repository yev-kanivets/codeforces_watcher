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
            val promiseProblemsEn = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("en")
            }

            val promiseProblemsRu = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("ru")
            }

            var problemsEn: List<Problem> = listOf()
            promiseProblemsEn.await()?.body()?.result?.problems?.let {
                problemsEn = it
            } ?: store.dispatch(Failure(null))

            promiseProblemsRu.await()?.body()?.result?.problems?.let { problemsRu ->
                store.dispatch(Success(mergeProblems(problemsEn, problemsRu)))
            } ?: store.dispatch(Failure(null))
        }

        private fun mergeProblems(problemsEn: List<Problem>, problemsRu: List<Problem>): List<Problem> {
            val problems: MutableList<Problem> = mutableListOf()
            for ((index, problem) in problemsEn.withIndex()) {
                problem.ruName = problemsRu[index].name
                problem.enName = problem.name
                problems.add(problem)
            }
            return problems
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}