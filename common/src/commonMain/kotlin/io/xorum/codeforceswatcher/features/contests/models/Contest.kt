package io.xorum.codeforceswatcher.features.contests.models

import io.xorum.codeforceswatcher.Contest

data class Contest(
        val id: Long,
        val name: String,
        val startTimeSeconds: Long,
        val durationSeconds: Long,
        val phase: String
) {
    companion object {
        fun fromDB(dbContest: Contest): io.xorum.codeforceswatcher.features.contests.models.Contest {
            return Contest(
                    id = dbContest.id,
                    name = dbContest.name,
                    startTimeSeconds = dbContest.startTimeSeconds,
                    durationSeconds = dbContest.durationSeconds,
                    phase = dbContest.phase
            )
        }
    }
}
