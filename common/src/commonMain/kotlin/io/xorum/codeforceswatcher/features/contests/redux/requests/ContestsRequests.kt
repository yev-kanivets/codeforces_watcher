package io.xorum.codeforceswatcher.features.contests.redux.requests

import com.soywiz.klock.DateTime
import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.features.contests.models.Platform
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import io.xorum.codeforceswatcher.network.KontestsApiClient
import io.xorum.codeforceswatcher.redux.Message
import io.xorum.codeforceswatcher.redux.Request
import io.xorum.codeforceswatcher.redux.ToastAction
import io.xorum.codeforceswatcher.redux.store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import tw.geothings.rekotlin.Action

class ContestsRequests {

    class FetchContests(val isInitiatedByUser: Boolean) : Request() {

        override suspend fun execute() {
            val responseCodeforces = CoroutineScope(Dispatchers.Main).async { CodeforcesApiClient.getCodeforcesContests() }
            val responseKontests = CoroutineScope(Dispatchers.Main).async { KontestsApiClient.getAllContests() }

            val codeforcesContests = responseCodeforces.await()?.result
            val allContests = responseKontests.await()

            if (codeforcesContests == null && allContests == null) {
                dispatchFailure()
                return
            }
            val contests: MutableList<Contest> = mutableListOf()
            codeforcesContests?.forEach {
                contests.add(it.copy(startTimeSeconds = it.startTimeSeconds * 1000))
            }
            allContests?.forEach {
                val contest = it.toContest()
                if (contest.platform != Platform.CODEFORCES && contest.startTimeSeconds >= DateTime.now().unixMillis) contests.add(contest)
            }
            store.dispatch(Success(contests))
        }

        private fun dispatchFailure() {
            store.dispatch(Failure(if (isInitiatedByUser) Message.NoConnection else Message.None))
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(override val message: Message) : ToastAction
    }
}
