package com.bogdan.codeforceswatcher.features.actions.redux.requests

import androidx.core.text.HtmlCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.getUsers
import com.bogdan.codeforceswatcher.features.users.models.UsersRequestResult
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import com.bogdan.codeforceswatcher.store
import io.xorum.codeforceswatcher.features.actions.models.CFAction
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.network.CodeforcesRestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.geothings.rekotlin.Action
import java.util.*

class ActionsRequests {

    class FetchActions(
            private val isInitializedByUser: Boolean
    ) : Request() {

        override suspend fun execute() {
            val response = CodeforcesRestClient.getActions(lang = defineLang())
            response?.result?.let { actions ->
                buildUiDataAndDispatch(actions)
            } ?: dispatchFailure()
        }

        private suspend fun buildUiDataAndDispatch(actions: List<CFAction>) {
            val handles = buildHandles(actions)

            when (val result = getUsers(handles, false)) {
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

        private fun buildHandles(actions: List<CFAction>): String {
            val handles: MutableSet<String> = mutableSetOf()

            for (action in actions) {
                action.comment?.let { handles.add(it.commentatorHandle) }
                handles.add(action.blogEntry.authorHandle)
            }

            return handles.joinToString(separator = ";")
        }

        private suspend fun buildUiData(
                actions: List<CFAction>,
                users: List<User>?
        ): List<CFAction> = withContext(Dispatchers.IO) {
            val uiData: MutableList<CFAction> = mutableListOf()

            for (action in actions) {
                action.comment?.let { comment ->
                    users?.find { user -> user.handle == comment.commentatorHandle }
                            ?.let { foundUser ->
                                comment.commentatorAvatar = foundUser.avatar
                                comment.commentatorRank = foundUser.rank
                            }

                    comment.text = convertFromHtml(comment.text)
                } ?: if (action.timeSeconds != action.blogEntry.creationTimeSeconds &&
                        action.timeSeconds != action.blogEntry.modificationTimeSeconds) {
                    continue
                }

                users?.find { user -> user.handle == action.blogEntry.authorHandle }
                        ?.let { foundUser ->
                            action.blogEntry.authorAvatar = foundUser.avatar
                            action.blogEntry.authorRank = foundUser.rank
                        }

                action.blogEntry.title = convertFromHtml(action.blogEntry.title)
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
