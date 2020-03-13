package com.bogdan.codeforceswatcher.features.add_user.redux.states

import tw.geothings.rekotlin.StateType

data class AddUserState(
        val status: Status = Status.IDLE
) : StateType {
    enum class Status { IDLE, PENDING, DONE }
}
