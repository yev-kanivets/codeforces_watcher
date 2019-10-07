package com.bogdan.codeforceswatcher.features.actions.models

data class BlogEntry(
    val id: Int,
    val title: String,
    val content: String?,
    val creationTimeSeconds: Long
)