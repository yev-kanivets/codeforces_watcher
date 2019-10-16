package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.CFAction
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.network.getUsers
import com.bogdan.codeforceswatcher.network.models.Error
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
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
                        buildUiDataAndDispatch(actions)
                    } ?: store.dispatch(Failure(if (isInitializedByUser) noConnection else null))
                }

                override fun onFailure(call: Call<ActionsResponse>, t: Throwable) {
                    store.dispatch(Failure(if (isInitializedByUser) noConnection else null))
                }
            })
        }

        private fun buildUiDataAndDispatch(actions: List<CFAction>) {
            val commentatorsHandles = buildCommentatorsHandles(actions)

            getUsers(commentatorsHandles, false) { result ->
                when (result) {
                    is UsersRequestResult.Success ->
                        store.dispatch(Success(buildUiData(actions, result.users)))
                    is UsersRequestResult.Failure -> dispatchError(result.error)
                }
            }
        }

        private fun buildCommentatorsHandles(actions: List<CFAction>): String {
            var commentatorsHandles = ""

            for (action in actions) {
                if (action.comment != null) {
                    commentatorsHandles += "${action.comment.commentatorHandle};"
                }
            }

            return commentatorsHandles
        }

        private fun buildUiData(actions: List<CFAction>, users: List<User>?): List<CFAction> {
            val uiData: MutableList<CFAction> = mutableListOf()

            for (action in actions) {
                if (action.comment == null) continue

                users?.find { user -> user.handle == action.comment.commentatorHandle }
                    ?.let { foundUser ->
                        action.comment.commentatorAvatar = foundUser.avatar
                        action.comment.commentatorRank = foundUser.rank
                    }

                uiData.add(action)
            }

            return uiData
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