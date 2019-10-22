package com.bogdan.codeforceswatcher.features.actions.models

data class BlogEntry(
    val id: Int,
    var title: String,
    val content: String?,
    val creationTimeSeconds: Long
)