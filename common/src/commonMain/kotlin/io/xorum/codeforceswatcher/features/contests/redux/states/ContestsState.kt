package io.xorum.codeforceswatcher.features.contests.redux.states

import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.features.contests.models.Platform
import tw.geothings.rekotlin.StateType

data class ContestsState(
        val status: Status = Status.IDLE,
        val contests: List<Contest> = listOf(),
        val filters: Set<Platform> = setOf()
) : StateType {

    enum class Status { IDLE, PENDING }
}
