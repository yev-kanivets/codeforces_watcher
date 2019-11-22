package com.bogdan.codeforceswatcher.features.problems.redux.requests

import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.CrashLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.rekotlin.Action
import java.lang.IllegalArgumentException

class ProblemsRequests {

    class FetchProblems : Request() {

        override suspend fun execute() {
            val promiseProblemsEn = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("en")
            }

            val promiseProblemsRu = CoroutineScope(Dispatchers.Main).async {
                RestClient.getProblems("ru")
            }

            val problemsEn = promiseProblemsEn.await()?.body()?.result?.problems
            val problemsRu = promiseProblemsRu.await()?.body()?.result?.problems

            if (problemsEn == null || problemsRu == null) {
                store.dispatch(Failure(null))
            } else {
                if (isProblemsMatching(problemsEn, problemsRu)) {
                    val problems = mergeProblems(problemsEn, problemsRu)
                    bindProblemsToContests(problems)
                    store.dispatch(Success(problems))
                } else {
                    CrashLogger.log(IllegalArgumentException("Problems doesn't match"))
                    store.dispatch(Failure(null))
                }
            }
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

        private fun bindProblemsToContests(problems: List<Problem>) {
            val contests = store.state.contests.contests
            for (problem in problems) {
                contests.find { it.id == problem.contestId }?.let { contest ->
                    problem.contestName = contest.name
                    problem.contestTime = contest.time
                }
            }
        }

        private fun isProblemsMatching(problemsEn: List<Problem>, problemsRu: List<Problem>): Boolean {
            for ((index, problem) in problemsEn.withIndex()) {
                if (problem.contestId != problemsRu[index].contestId ||
                    problem.index != problemsRu[index].index) {
                    return false
                }
            }
            return true
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}