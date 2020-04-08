package io.xorum.codeforceswatcher.network.responses

import kotlinx.serialization.Serializable

@Serializable
data class PinnedPost(
        val title: String,
        val link: String
)