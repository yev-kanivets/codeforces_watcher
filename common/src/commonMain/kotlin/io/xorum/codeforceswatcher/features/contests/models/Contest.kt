package io.xorum.codeforceswatcher.features.contests.models

import io.xorum.codeforceswatcher.DbContest

data class Contest(
        val id: Long,
        val name: String,
        val startTimeSeconds: Long,
        val durationSeconds: Long,
        val phase: String
) {
    companion object {
        fun fromDB(dbContest: DbContest) = Contest(
                id = dbContest.id,
                name = dbContest.name,
                startTimeSeconds = dbContest.time,
                durationSeconds = dbContest.duration,
                phase = dbContest.phase
        )
    }
}
