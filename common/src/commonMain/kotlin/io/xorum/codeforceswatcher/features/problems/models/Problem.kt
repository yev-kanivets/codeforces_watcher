package io.xorum.codeforceswatcher.features.problems.models

import io.xorum.codeforceswatcher.Problem
import kotlinx.serialization.Serializable

@Serializable
class Problem(
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

    fun copy(id: Long = this.id, name: String = this.name, enName: String = this.enName, ruName: String = this.ruName, index: String = this.index, contestId: Long = this.contestId, contestName: String = this.contestName, contestTime: Long = this.contestTime, isFavourite: Boolean = this.isFavourite) =
            Problem(id, name, enName, ruName, index, contestId, contestName, contestTime, isFavourite)

    companion object {
        fun fromDB(dbProblem: Problem): io.xorum.codeforceswatcher.features.problems.models.Problem {
            return Problem(
                    id = dbProblem.id,
                    name = dbProblem.name,
                    enName = dbProblem.enName,
                    ruName = dbProblem.ruName,
                    index = dbProblem.indexProblem,
                    contestId = dbProblem.contestId,
                    contestName = dbProblem.contestName,
                    contestTime = dbProblem.contestTime,
                    isFavourite = dbProblem.isFavourite
            )
        }
    }
}