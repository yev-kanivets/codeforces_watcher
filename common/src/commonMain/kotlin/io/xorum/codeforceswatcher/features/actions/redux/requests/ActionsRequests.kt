package io.xorum.codeforceswatcher.features.actions.redux.requests

import io.xorum.codeforceswatcher.features.actions.models.CFAction
import io.xorum.codeforceswatcher.features.users.models.User
import io.xorum.codeforceswatcher.features.users.redux.getUsers
import io.xorum.codeforceswatcher.features.users.redux.models.UsersRequestResult
import io.xorum.codeforceswatcher.network.PinnedPostsApiClient
import io.xorum.codeforceswatcher.network.responses.PinnedPost
import io.xorum.codeforceswatcher.redux.*
import io.xorum.codeforceswatcher.util.settings
import tw.geothings.rekotlin.Action

class ActionsRequests {

    class FetchActions(
            private val isInitializedByUser: Boolean,
            private val language: String
    ) : Request() {

        override suspend fun execute() {
            val response = codeforcesRepository.getActions(lang = defineLang())
            response?.result?.let { actions ->
                buildUiDataAndDispatch(actions)
            } ?: dispatchFailure()
        }

        private suspend fun buildUiDataAndDispatch(actions: List<CFAction>) {
            val handles = buildHandles(actions)

            when (val result = getUsers(handles, false, defineLang())) {
                is UsersRequestResult.Success -> {
                    store.dispatch(Success(buildUiData(actions, result.users)))
                }
                is UsersRequestResult.Failure -> dispatchFailure()
            }
        }

        private fun dispatchFailure() {
            val noConnectionError = if (isInitializedByUser) Message.NoConnection else Message.None
            store.dispatch(Failure(noConnectionError))
        }

        private fun buildHandles(actions: List<CFAction>) = actions.flatMap { action ->
            listOf(action.comment?.commentatorHandle, action.blogEntry.authorHandle)
        }.filterNotNull().toSet().joinToString(separator = ";")

        private fun buildUiData(
                actions: List<CFAction>,
                users: List<User>?
        ): List<CFAction> {
            val uiData: MutableList<CFAction> = mutableListOf()

            for (action in actions) {
                action.comment?.let { comment ->
                    users?.find { user -> user.handle == comment.commentatorHandle }
                            ?.let { foundUser ->
                                comment.commentatorAvatar = foundUser.avatar
                                comment.commentatorRank = foundUser.rank
                            }
                } ?: if (isUnnecessaryAction(action)) continue

                users?.find { user -> user.handle == action.blogEntry.authorHandle }
                        ?.let { foundUser ->
                            action.blogEntry.authorAvatar = foundUser.avatar
                            action.blogEntry.authorRank = foundUser.rank
                        }

                uiData.add(action)
            }

            return uiData
        }

        private fun isUnnecessaryAction(action: CFAction) =
                (action.timeSeconds != action.blogEntry.creationTimeSeconds &&
                        action.timeSeconds != action.blogEntry.modificationTimeSeconds)

        private fun defineLang(): String {
            return if (language == "ru" || language == "uk") "ru" else "en"
        }

        data class Success(val actions: List<CFAction>) : Action

        data class Failure(override val message: Message) : ToastAction
    }

    class FetchPinnedPost : Request() {
        override suspend fun execute() {
            val response = pinnedPostsApiClient.getPinnedPost()
            response?.let {
                store.dispatch(Success(it))
            } ?: store.dispatch(Failure(Message.Custom("Failed to fetch pinned post")))
        }

        data class Success(val pinnedPost: PinnedPost) : Action

        data class Failure(override val message: Message) : ToastAction
    }

    class RemovePinnedPost(val link: String) : Request() {
        override suspend fun execute() {
            settings.writePinnedPostLink(link)
        }
    }
}
