package com.bogdan.codeforceswatcher.features.actions.redux.states

import com.bogdan.codeforceswatcher.features.actions.models.Action
import org.rekotlin.StateType

data class ActionsState(
    val status: Status = Status.IDLE,
    val actions: List<Action> = listOf()
) : StateType {

    enum class Status { IDLE, PENDING }
}