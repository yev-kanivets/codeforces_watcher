package io.xorum.codeforceswatcher.network.responses

import io.xorum.codeforceswatcher.features.users.models.RatingChange
import kotlinx.serialization.Serializable

@Serializable
data class RatingChangeResponse(
        val status: String,
        val result: List<RatingChange>? = null,
        val comment: String? = null
)