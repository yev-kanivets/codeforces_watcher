package com.bogdan.codeforceswatcher.features.contests.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.features.contests.models.Contest
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import io.xorum.codeforceswatcher.network.CodeforcesApiClient
import tw.geothings.rekotlin.Action

class ContestsRequests {

    class FetchContests(val isInitiatedByUser: Boolean) : Request() {

        override suspend fun execute() {
            val noConnectionError = CwApp.app.getString(R.string.no_connection)
            val response = CodeforcesApiClient.getContests()
            response?.result?.let { contests ->
                store.dispatch(Success(contests))
            } ?: store.dispatch(
                    Failure(if (isInitiatedByUser) noConnectionError else null)
            )
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(
                override val message: String?
        ) : ToastAction
    }
}
