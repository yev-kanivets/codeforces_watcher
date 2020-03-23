package io.xorum.codeforceswatcher.features.actions.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
        val id: Long,
        var text: String,
        val commentatorHandle: String,
        var commentatorAvatar: String? = null,
        var commentatorRank: String? = null
)