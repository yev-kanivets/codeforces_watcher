package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.Action
import com.bogdan.codeforceswatcher.network.Error
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.network.getUsers
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

                val noConnection = CwApp.app.getString(R.string.no_connection)

                override fun onResponse(
                    call: Call<ActionsResponse>,
                    response: Response<ActionsResponse>
                ) {
                    response.body()?.actions?.let { actions ->
                        formUIDataAndDispatch(actions)
                    } ?: store.dispatch(Failure(noConnection))
                }

                override fun onFailure(call: Call<ActionsResponse>, t: Throwable) {
                    store.dispatch(Failure(noConnection))
                }
            })
        }

        private fun formUIDataAndDispatch(actions: List<Action>) {
            val uiData: MutableList<Action> = mutableListOf()
            var commentatorsHandles = ""
            for (action in actions) {
                if (action.comment != null) {
                    commentatorsHandles += "${action.comment.commentatorHandle};"
                }
            }

            getUsers(commentatorsHandles, false) {
                val error = it.second
                val users = it.first
                if (error == null) {
                    for (action in actions) {
                        action.comment?.commentatorAvatar =
                            users?.find { user -> user.handle == action.comment?.commentatorHandle }?.avatar
                        if (action.comment != null) uiData.add(action)
                    }
                    store.dispatch(Success(uiData))
                } else {
                    dispatchError(error)
                }
            }
        }

        private fun dispatchError(error: Error) {
            when (error) {
                Error.INTERNET ->
                    store.dispatch(
                        Failure(CwApp.app.resources.getString(R.string.no_connection))
                    )
                Error.RESPONSE ->
                    store.dispatch(
                        Failure(CwApp.app.resources.getString(R.string.no_connection))
                    )
            }
        }

        data class Success(val actions: List<Action>) : org.rekotlin.Action

        data class Failure(override val message: String?) : ToastAction
    }
}
