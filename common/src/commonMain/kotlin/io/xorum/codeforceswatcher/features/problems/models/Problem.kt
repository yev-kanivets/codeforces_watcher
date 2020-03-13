package io.xorum.codeforceswatcher.features.problems.models

import io.xorum.codeforceswatcher.DbProblem
import kotlinx.serialization.Serializable

@Serializable
data class Problem(
        var id: Long,
        val name: String,
        var enName: String,
        var ruName: String,
        val index: String,
        var contestId: Long,
        var contestName: String,
        var contestTime: Long,
        var isFavourite: Boolean = false
) {
    fun identify() = "$contestId$index"

    companion object {
        fun fromDB(dbProblem: DbProblem) = Problem(
                id = dbProblem.id,
                name = dbProblem.name,
                enName = dbProblem.enName,
                ruName = dbProblem.ruName,
                index = dbProblem.index,
                contestId = dbProblem.contestId,
                contestName = dbProblem.contestName,
                contestTime = dbProblem.contestTime,
                isFavourite = dbProblem.isFavourite
        )
    }
}