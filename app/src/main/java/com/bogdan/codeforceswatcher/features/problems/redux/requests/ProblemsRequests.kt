package com.bogdan.codeforceswatcher.features.problems.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.room.DatabaseClient
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.CrashLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.rekotlin.Action

class ProblemsRequests {

    class FetchProblems(
        private val isInitializedByUser: Boolean
    ) : Request() {

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
                dispatchFailure()
            } else {
                if (isProblemsMatching(problemsEn, problemsRu)) {
                    val problems = mergeProblems(problemsEn, problemsRu)
                    withContext(Dispatchers.IO) { bindProblemsToContests(problems) }
                    updateDatabaseAndDispatch(problems)
                } else {
                    CrashLogger.log(IllegalArgumentException("Problems doesn't match"))
                    dispatchFailure()
                }
            }
        }

        private fun dispatchFailure() {
            val noConnectionError = if (isInitializedByUser) {
                CwApp.app.getString(R.string.no_connection)
            } else {
                null
            }
            store.dispatch(Failure(noConnectionError))
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

        private suspend fun updateDatabaseAndDispatch(newProblems: List<Problem>) {
            withContext(Dispatchers.IO) {
                val isFavouriteProblem = hashMapOf<String, Boolean>()
                val problems = DatabaseClient.problemsDao.getAll()
                for (problem in problems) {
                    isFavouriteProblem[problem.name] = problem.isFavourite
                }
                for (problem in newProblems) {
                    problem.isFavourite = isFavouriteProblem[problem.name] ?: false
                }
                DatabaseClient.problemsDao.insert(newProblems)
            }
            store.dispatch(Success(newProblems))
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }

    class MarkProblemFavorite(val problem: Problem) : Request() {

        override suspend fun execute() {
            lateinit var newProblem: Problem
            withContext(Dispatchers.IO) {
                DatabaseClient.problemsDao.delete(problem)
                newProblem = problem.copy(isFavourite = !problem.isFavourite)
                DatabaseClient.problemsDao.insert(newProblem)
            }
            store.dispatch(Success(newProblem))
        }

        data class Success(val problem: Problem) : Action
    }
}