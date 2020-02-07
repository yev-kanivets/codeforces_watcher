package com.bogdan.codeforceswatcher.features.actions.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CFAction(
        @SerializedName("timeSeconds")
        val timeInMillisecond: Long,
        val blogEntry: BlogEntry,
        val comment: Comment?
) : Serializable