package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.Action
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActionsRequests {

    class FetchActions : Request() {

        override fun execute() {
            RestClient.getActions().enqueue(object : Callback<ActionsResponse> {

                val noInternetConnectionError = CwApp.app.getString(R.string.no_connection)

                override fun onResponse(
                    call: Call<ActionsResponse>,
                    response: Response<ActionsResponse>
                ) {
                    response.body()?.actions?.let { actions ->

                        //TODO upload content blogEntry and user's images
                        store.dispatch(Success(actions))
                    } ?: store.dispatch(Failure(noInternetConnectionError))
                }

                override fun onFailure(call: Call<ActionsResponse>, t: Throwable) {
                    store.dispatch(Failure(noInternetConnectionError))
                }
            })
        }


        data class Success(val actions: List<Action>) : org.rekotlin.Action

        data class Failure(override val message: String?) : ToastAction
    }
}
