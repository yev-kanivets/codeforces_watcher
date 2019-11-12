package com.bogdan.codeforceswatcher.features.actions.redux.requests

import androidx.core.text.HtmlCompat
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
import kotlinx.coroutines.*
import org.rekotlin.Action
import java.util.*

class ActionsRequests {

    class FetchActions(
        private val isInitializedByUser: Boolean
    ) : Request() {

        override fun execute() {
            CoroutineScope(Dispatchers.Main).launch {
                val noConnectionError = CwApp.app.getString(R.string.no_connection)
                try {
                    val response = RestClient.getActions(lang = defineLang(Locale.getDefault().language))
                    response.body()?.actions?.let { actions ->
                        buildUiDataAndDispatch(actions)
                    }
                        ?: store.dispatch(Failure(if (isInitializedByUser) noConnectionError else null))
                } catch (t: Throwable) {
                    store.dispatch(Failure(if (isInitializedByUser) noConnectionError else null))
                }
            }
        }

        private fun buildUiDataAndDispatch(actions: List<CFAction>) {
            val commentatorsHandles = buildCommentatorsHandles(actions)

            getUsers(commentatorsHandles, false) { result ->
                when (result) {
                    is UsersRequestResult.Success ->
                        GlobalScope.launch {
                            val uiData = buildUiData(actions, result.users)
                            withContext(Dispatchers.Main) {
                                store.dispatch(Success(uiData))
                            }
                        }.start()
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
                action.blogEntry.title = convertFromHtml(action.blogEntry.title)
                action.comment.text = convertFromHtml(action.comment.text)

                uiData.add(action)
            }

            return uiData
        }

        private fun convertFromHtml(text: String) =
            HtmlCompat.fromHtml(text
                .replace("\n", "<br>")
                .replace("\t", "<tl>")
                .replace("$", ""),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
                .trim().toString()

        private fun dispatchError(error: Error) {
            val noConnectionError = CwApp.app.resources.getString(R.string.no_connection)
            when (error) {
                Error.INTERNET, Error.RESPONSE ->
                    store.dispatch(
                        Failure(if (isInitializedByUser) noConnectionError else null)
                    )
            }
        }

        private fun defineLang(locale: String) =
            if (locale == "ru" || locale == "uk") "ru" else "en"

        data class Success(val actions: List<CFAction>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}