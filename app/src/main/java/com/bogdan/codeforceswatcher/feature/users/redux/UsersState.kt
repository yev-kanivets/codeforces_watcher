package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.model.User
import org.rekotlin.StateType

data class UsersState(
    val status: Status = Status.IDLE,
    val users: List<User> = listOf(),
    val result: List<Pair<String, Int>> = listOf()
) : StateType {

    enum class Status { IDLE, PENDING }
}
