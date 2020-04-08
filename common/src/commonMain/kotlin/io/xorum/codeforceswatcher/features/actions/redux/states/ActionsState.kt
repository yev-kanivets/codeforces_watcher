package io.xorum.codeforceswatcher.features.actions.redux.states

import io.xorum.codeforceswatcher.features.actions.models.CFAction
import tw.geothings.rekotlin.StateType

data class ActionsState(
    val status: Status = Status.IDLE,
    val actions: List<CFAction> = listOf()
) : StateType {

    enum class Status { IDLE, PENDING }
}