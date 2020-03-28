package io.xorum.codeforceswatcher.features.contests.redux.requests

import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import io.xorum.codeforceswatcher.redux.Request
import io.xorum.codeforceswatcher.redux.ToastAction
import io.xorum.codeforceswatcher.redux.localizedStrings
import io.xorum.codeforceswatcher.redux.store
import tw.geothings.rekotlin.Action

class ContestsRequests {

    class FetchContests(val isInitiatedByUser: Boolean) : Request() {

        override suspend fun execute() {
            val noConnectionError = localizedStrings["No connection"]
            val response = CodeforcesApiClient.getContests()
            response?.result?.let { contests ->
                store.dispatch(Success(contests))
            } ?: store.dispatch(Failure(if (isInitiatedByUser) noConnectionError else null))
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}
