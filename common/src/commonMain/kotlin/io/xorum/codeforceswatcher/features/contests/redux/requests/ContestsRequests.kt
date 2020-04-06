package io.xorum.codeforceswatcher.features.contests.redux.requests

import com.soywiz.klock.DateTime
import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.features.contests.models.Platform
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import io.xorum.codeforceswatcher.network.KontestsApiClient
import io.xorum.codeforceswatcher.network.responses.ContestResponse
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
            val responseCodeforces = CoroutineScope(Dispatchers.Default).async { CodeforcesApiClient.getCodeforcesContests() }
            val responseKontests = CoroutineScope(Dispatchers.Default).async { KontestsApiClient.getAllContests() }

            val contests = normalizeCodeforcesContests(responseCodeforces.await()?.result) + normalizeAllContests(responseKontests.await())

            if (contests.isEmpty()) {
                dispatchFailure()
            } else {
                store.dispatch(Success(contests))
            }
        }

        private fun normalizeCodeforcesContests(contests: List<Contest>?) = contests?.map { it.copy(startTimeSeconds = it.startTimeSeconds * 1000) }.orEmpty()

        private fun normalizeAllContests(contests: List<ContestResponse>?) = contests?.filter {
            val contest = it.toContest()
            contest.platform != Platform.CODEFORCES && contest.startTimeSeconds >= DateTime.now().unixMillis
        }?.map { it.toContest() }.orEmpty()

        private fun dispatchFailure() {
            store.dispatch(Failure(if (isInitiatedByUser) Message.NoConnection else Message.None))
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(override val message: Message) : ToastAction
    }

    class ChangeFilterCheckStatus(val platform: Platform, val isChecked: Boolean) : Action
}
