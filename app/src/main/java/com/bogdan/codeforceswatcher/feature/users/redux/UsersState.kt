package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.model.User
import org.rekotlin.StateType

data class UsersState(
    val status: Status = Status.IDLE,
    val users: List<User> = listOf(),
    val result: List<Pair<String, Int>> = listOf(),
    val sortType: SortType = SortType.DEFAULT
) : StateType {

    enum class Status { IDLE, PENDING }
    enum class SortType { DEFAULT, RATING_UP, RATING_DOWN, UPDATE_UP, UPDATE_DOWN }
}
