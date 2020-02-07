package com.bogdan.codeforceswatcher.features.actions.models

import java.io.Serializable

data class Comment(
        val id: Long,
        var text: String,
        val commentatorHandle: String,
        var commentatorAvatar: String,
        var commentatorRank: String?
) : Serializable