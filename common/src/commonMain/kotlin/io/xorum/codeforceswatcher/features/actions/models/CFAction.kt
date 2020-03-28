package io.xorum.codeforceswatcher.features.actions.models

import kotlinx.serialization.Serializable

@Serializable
data class CFAction(
        val timeSeconds: Long,
        val blogEntry: BlogEntry,
        val comment: Comment? = null
) {
    val id = hashCode()
}
