package io.xorum.codeforceswatcher.features.actions.redux.states

import io.xorum.codeforceswatcher.features.actions.models.CFAction
import io.xorum.codeforceswatcher.network.responses.PinnedPost
import tw.geothings.rekotlin.StateType

data class ActionsState(
        val status: Status = Status.IDLE,
        val actions: List<CFAction> = listOf(),
        val pinnedPost: PinnedPost? = null
) : StateType {

    enum class Status { IDLE, PENDING }
}
