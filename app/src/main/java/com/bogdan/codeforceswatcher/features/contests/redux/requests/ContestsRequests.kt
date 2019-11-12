package com.bogdan.codeforceswatcher.features.contests.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.contests.models.Contest
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rekotlin.Action

class ContestsRequests {

    class FetchContests(val isInitiatedByUser: Boolean) : Request() {

        override fun execute() {
            CoroutineScope(Dispatchers.Main).launch {
                val noInternetConnectionError = CwApp.app.getString(R.string.no_connection)
                try {
                    val response = RestClient.getContests()

                    response.body()?.contests?.let { contests ->
                        store.dispatch(Success(contests))
                    } ?: store.dispatch(
                        Failure(if (isInitiatedByUser) noInternetConnectionError else null)
                    )
                } catch (t: Throwable) {
                    store.dispatch(
                        Failure(if (isInitiatedByUser) noInternetConnectionError else null)
                    )
                }
            }
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(
            override val message: String?
        ) : ToastAction
    }
}
