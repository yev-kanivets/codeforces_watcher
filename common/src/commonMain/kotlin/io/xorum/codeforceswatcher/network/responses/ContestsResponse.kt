package io.xorum.codeforceswatcher.network.responses

import io.xorum.codeforceswatcher.features.contests.models.Contest
import kotlinx.serialization.Serializable

@Serializable
data class ContestsResponse(
        val status: String,
        val result: List<Contest>
)