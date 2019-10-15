package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.CFAction
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

    class FetchActions(
        val isInitializedByUser: Boolean
    ) : Request() {

        override fun execute() {
            RestClient.getActions().enqueue(object : Callback<ActionsResponse> {
                val noConnection = CwApp.app.getString(R.string.no_connection)

                override fun onResponse(
                    call: Call<ActionsResponse>,
                    response: Response<ActionsResponse>
                ) {
                    response.body()?.actions?.let { actions ->
                        formUIDataAndDispatch(actions)
                    } ?: store.dispatch(Failure(if (isInitializedByUser) noConnection else null))
                }

                override fun onFailure(call: Call<ActionsResponse>, t: Throwable) {
                    store.dispatch(Failure(if (isInitializedByUser) noConnection else null))
                }
            })
        }

        private fun formUIDataAndDispatch(actions: List<CFAction>) {
            val uiData: MutableList<CFAction> = mutableListOf()
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
                        if (action.comment != null) {
                            users?.find { user -> user.handle == action.comment.commentatorHandle }
                                ?.let { foundUser ->
                                    action.comment.commentatorAvatar = foundUser.avatar
                                    action.comment.commentatorRank = foundUser.rank
                                }
                            uiData.add(action)
                        }
                    }
                    store.dispatch(Success(uiData))
                } else {
                    dispatchError(error)
                }
            }
        }

        private fun dispatchError(error: Error) {
            val noConnectionError = CwApp.app.resources.getString(R.string.no_connection)
            when (error) {
                Error.INTERNET, Error.RESPONSE ->
                    store.dispatch(
                        Failure(if (isInitializedByUser) noConnectionError else null)
                    )
            }
        }

        data class Success(val actions: List<CFAction>) : org.rekotlin.Action

        data class Failure(override val message: String?) : ToastAction
    }
}
