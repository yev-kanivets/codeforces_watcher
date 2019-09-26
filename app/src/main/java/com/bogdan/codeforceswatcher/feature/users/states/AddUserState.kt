package com.bogdan.codeforceswatcher.feature.users.states

import org.rekotlin.StateType

data class AddUserState(
    val status: Status = Status.IDLE
) : StateType {
    enum class Status { IDLE, PENDING, DONE }
}