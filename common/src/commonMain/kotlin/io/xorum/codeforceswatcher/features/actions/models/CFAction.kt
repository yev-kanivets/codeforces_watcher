package io.xorum.codeforceswatcher.features.actions.models

import kotlinx.serialization.Serializable

@Serializable
data class CFAction(
        val timeSeconds: Long,
        val blogEntry: BlogEntry,
        val comment: Comment? = null
) {
    val id: Int
        get() = hashCode()

    val link: String
        get() = comment?.let {
            "https://codeforces.com/blog/entry/${blogEntry.id}?#comment-${comment.id}"
        } ?: "https://codeforces.com/blog/entry/${blogEntry.id}"
}
