package com.bogdan.codeforceswatcher.features.actions.redux.requests

import androidx.core.text.HtmlCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.CFAction
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.network.getUsers
import com.bogdan.codeforceswatcher.network.models.UsersRequestResult
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rekotlin.Action
import java.util.Locale

class ActionsRequests {

    class FetchActions(
        private val isInitializedByUser: Boolean
    ) : Request() {

        override suspend fun execute() {
            try {
                val response = RestClient.getActions(lang = defineLang())
                response.body()?.actions?.let { actions ->
                    buildUiDataAndDispatch(actions)
                } ?: dispatchFailure()
            } catch (t: Throwable) {
                dispatchFailure()
            }
        }

        private suspend fun buildUiDataAndDispatch(actions: List<CFAction>) {
            val commentatorsHandles = buildCommentatorsHandles(actions)

            when (val result = getUsers(commentatorsHandles, false)) {
                is UsersRequestResult.Success -> {
                    store.dispatch(Success(buildUiData(actions, result.users)))
                }
                is UsersRequestResult.Failure -> dispatchFailure()
            }
        }

        private fun dispatchFailure() {
            val noConnectionError = if (isInitializedByUser) {
                CwApp.app.getString(R.string.no_connection)
            } else {
                null
            }

            store.dispatch(Failure(noConnectionError))
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

        private suspend fun buildUiData(
            actions: List<CFAction>,
            users: List<User>?
        ): List<CFAction> = withContext(Dispatchers.IO) {
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

            uiData
        }

        private fun convertFromHtml(text: String) =
            HtmlCompat.fromHtml(
                text.replace("\n", "<br>")
                    .replace("\t", "<tl>")
                    .replace("$", ""), HtmlCompat.FROM_HTML_MODE_LEGACY
            ).trim().toString()

        private fun defineLang(): String {
            val locale = Locale.getDefault().language
            return if (locale == "ru" || locale == "uk") "ru" else "en"
        }

        data class Success(val actions: List<CFAction>) : Action

        data class Failure(override val message: String?) : ToastAction
    }
}
