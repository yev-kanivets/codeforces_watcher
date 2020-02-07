package com.bogdan.codeforceswatcher.features.add_user.redux.states

import org.rekotlin.StateType

data class AddUserState(
        val status: Status = Status.IDLE
) : StateType {
    enum class Status { IDLE, PENDING, DONE }
}