package com.bogdan.codeforceswatcher.features.problems.redux.states

import io.xorum.codeforceswatcher.features.problems.models.Problem
import tw.geothings.rekotlin.StateType

data class ProblemsState(
        val problems: List<Problem> = listOf(),
        val status: Status = Status.IDLE,
        val isFavourite: Boolean = false
) : StateType {

    enum class Status { IDLE, PENDING }
}
