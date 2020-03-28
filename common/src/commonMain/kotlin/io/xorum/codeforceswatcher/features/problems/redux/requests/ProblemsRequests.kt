package io.xorum.codeforceswatcher.features.problems.redux.requests

import io.xorum.codeforceswatcher.db.DatabaseQueries
import io.xorum.codeforceswatcher.features.problems.models.Problem
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import kotlinx.coroutines.*
import io.xorum.codeforceswatcher.redux.Request
import io.xorum.codeforceswatcher.redux.ToastAction
import io.xorum.codeforceswatcher.redux.localizedStrings
import io.xorum.codeforceswatcher.redux.store
import tw.geothings.rekotlin.Action

interface CrashLogger {
    fun log(t: Throwable)
}

var crashLogger: CrashLogger? = null

class ProblemsRequests {

    class FetchProblems(
            private val isInitializedByUser: Boolean
    ) : Request() {

        override suspend fun execute() {
            if (!isInitializedByUser) delay(1000)
            val promiseProblemsEn = CoroutineScope(Dispatchers.Main).async {
                CodeforcesApiClient.getProblems("en")
            }

            val promiseProblemsRu = CoroutineScope(Dispatchers.Main).async {
                CodeforcesApiClient.getProblems("ru")
            }

            val problemsEn = promiseProblemsEn.await()?.result?.problems
            val problemsRu = promiseProblemsRu.await()?.result?.problems

            if (problemsEn == null || problemsRu == null) {
                dispatchFailure()
            } else {
                if (isProblemsMatching(problemsEn, problemsRu)) {
                    var problems = listOf<Problem>()
                    withContext(Dispatchers.Default) {
                        problems = mergeProblems(problemsEn, problemsRu)
                        boundProblemsToContests(problems)
                        updateDatabase(problems)
                    }
                    store.dispatch(Success(problems))
                } else {
                    crashLogger?.log(IllegalArgumentException("Problems doesn't match"))
                    dispatchFailure()
                }
            }
        }

        private fun dispatchFailure() {
            val noConnectionError = if (isInitializedByUser) {
                localizedStrings["No connection"]
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

        private fun boundProblemsToContests(problems: List<Problem>) {
            val contests = store.state.contests.contests
            val mapContests = contests.associateBy { contest -> contest.id }
            problems.forEach { problem ->
                problem.contestName = mapContests[problem.contestId]?.name.orEmpty()
                problem.contestTime = mapContests[problem.contestId]?.startTimeSeconds ?: 0
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

        private fun updateDatabase(newProblems: List<Problem>) {
            val problems = DatabaseQueries.Problems.getAll()
            val favouriteProblemsMap = problems.associate { problem -> problem.identify() to problem.isFavourite }
            DatabaseQueries.Problems.deleteAll()

            newProblems.forEach { problem ->
                problem.isFavourite = favouriteProblemsMap[problem.identify()] ?: false
            }

            val identifiers = DatabaseQueries.Problems.insert(newProblems)
            newProblems.forEachIndexed { index, problem -> problem.id = identifiers[index] }
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }

    class ChangeStatusFavourite(private val problem: Problem) : Request() {

        override suspend fun execute() {
            val newProblem = problem.copy(isFavourite = !problem.isFavourite)
            withContext(Dispatchers.Default) {
                DatabaseQueries.Problems.insert(newProblem)
            }
            store.dispatch(Success(newProblem))
        }

        data class Success(val problem: Problem) : Action
    }
}
