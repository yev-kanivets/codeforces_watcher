package io.xorum.codeforceswatcher.network.responses

import io.xorum.codeforceswatcher.features.actions.models.CFAction
import kotlinx.serialization.Serializable

@Serializable
data class ActionsResponse(
        val status: String,
        val result: List<CFAction>
)