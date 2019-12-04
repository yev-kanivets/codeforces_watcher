package com.bogdan.codeforceswatcher.features.actions.models

import java.io.Serializable

data class BlogEntry(
    val id: Int,
    var title: String,
    val content: String?,
    val authorHandle: String,
    var authorRank: String?,
    var authorAvatar: String,
    val creationTimeSeconds: Long,
    val modificationTimeSeconds: Long
) : Serializable