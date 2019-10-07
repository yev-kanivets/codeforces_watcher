package com.bogdan.codeforceswatcher.features.actions.models

data class Comment(
    val id: Int,
    val text: String,
    val commentatorHandle: String,
    val creationTimeSeconds: Long
)