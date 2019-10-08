package com.bogdan.codeforceswatcher.features.actions.models

import com.google.gson.annotations.SerializedName

data class Action(
    @SerializedName("timeSeconds")
    val time: Long,
    val blogEntry: BlogEntry,
    val comment: Comment?
)