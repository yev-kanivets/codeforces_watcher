package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.feature.contests.ContestResponse
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.store
import org.rekotlin.Action
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestsRequests {

    class FetchContests : Request() {

        override fun execute() {
            RestClient.getContests().enqueue(object : Callback<ContestResponse> {

                override fun onResponse(
                    call: Call<ContestResponse>,
                    response: Response<ContestResponse>
                ) {
                    response.body()?.result?.let { contests ->
                        store.dispatch(Success(contests))
                    } ?: store.dispatch(Failure())
                }

                override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
                    store.dispatch(Failure(t))
                }
            })
        }

        data class Success(val contests: List<Contest>) : Action

        data class Failure(val t: Throwable? = null) : Action
    }
}
