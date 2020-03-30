package io.xorum.codeforceswatcher.features.actions.models

import kotlinx.serialization.Serializable

@Serializable
data class BlogEntry(
        val id: Int,
        var title: String,
        val content: String? = null,
        val authorHandle: String,
        var authorRank: String? = null,
        var authorAvatar: String? = null,
        val creationTimeSeconds: Long,
        val modificationTimeSeconds: Long
)
