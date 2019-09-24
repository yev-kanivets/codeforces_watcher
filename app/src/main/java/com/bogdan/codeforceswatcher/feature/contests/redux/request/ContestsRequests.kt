package com.bogdan.codeforceswatcher.feature.contests.redux.request

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.ErrorAction
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.store
import org.rekotlin.Action
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestsRequests {

    class FetchContests(val isInitiatedByUser: Boolean) : Request() {

        override fun execute() {
            RestClient.getContests().enqueue(object : Callback<ContestsResponse> {

                override fun onResponse(
                    call: Call<ContestsResponse>,
                    response: Response<ContestsResponse>
                ) {
                    response.body()?.result?.let { contests ->
                        store.dispatch(Success(contests))
                    }
                        ?: store.dispatch(
                            Failure(if (isInitiatedByUser) CwApp.app.getString(R.string.no_internet_connection) else null)
                        )
                }

                override fun onFailure(call: Call<ContestsResponse>, t: Throwable) {
                    store.dispatch(
                        Failure(if (isInitiatedByUser) CwApp.app.getString(R.string.no_internet_connection) else null)
                    )
                }
            })
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(
            override val message: String?
        ) : ErrorAction
    }
}
