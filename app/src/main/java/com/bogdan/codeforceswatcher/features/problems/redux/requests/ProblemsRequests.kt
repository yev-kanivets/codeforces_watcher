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
import kotlinx.coroutines.*
import tw.geothings.rekotlin.Action

class ProblemsRequests {

    class FetchProblems(
            private val isInitializedByUser: Boolean
    ) : Request() {

        override suspend fun execute() {
            if (!isInitializedByUser) delay(1000)
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
                    var problems = listOf<Problem>()
                    withContext(Dispatchers.IO) {
                        problems = mergeProblems(problemsEn, problemsRu)
                        boundProblemsToContests(problems)
                        updateDatabase(problems)
                    }
                    store.dispatch(Success(problems))
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

        private fun boundProblemsToContests(problems: List<Problem>) {
            val contests = store.state.contests.contests
            val mapContests = contests.associateBy { contest -> contest.id }
            problems.forEach { problem ->
                problem.contestName = mapContests[problem.contestId]?.name.orEmpty()
                problem.contestTime = mapContests[problem.contestId]?.time ?: 0
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
            val problems = DatabaseClient.problemsDao.getAll()
            val favouriteProblemsMap = problems.associate { problem -> identify(problem) to problem.isFavourite }
            DatabaseClient.problemsDao.deleteAll()

            newProblems.forEach { problem ->
                problem.isFavourite = favouriteProblemsMap[identify(problem)] ?: false
            }

            val identifiers = DatabaseClient.problemsDao.insert(newProblems)
            newProblems.forEachIndexed { index, problem -> problem.id = identifiers[index] }
        }

        private fun identify(problem: Problem): String {
            return problem.contestId.toString() + problem.index
        }

        data class Success(val problems: List<Problem>) : Action

        data class Failure(override val message: String?) : ToastAction
    }

    class ChangeStatusFavourite(val problem: Problem) : Request() {

        override suspend fun execute() {
            lateinit var newProblem: Problem
            withContext(Dispatchers.IO) {
                newProblem = problem.copy(isFavourite = !problem.isFavourite)
                DatabaseClient.problemsDao.insert(newProblem)
            }
            store.dispatch(Success(newProblem))
        }

        data class Success(val problem: Problem) : Action
    }
}
