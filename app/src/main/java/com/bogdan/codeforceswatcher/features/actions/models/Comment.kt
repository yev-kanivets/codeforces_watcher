package com.bogdan.codeforceswatcher.features.actions.models

data class Comment(
    val id: Long,
    var text: String,
    val commentatorHandle: String,
    val creationTimeSeconds: Long,
    var commentatorAvatar: String,
    var commentatorRank: String?
)