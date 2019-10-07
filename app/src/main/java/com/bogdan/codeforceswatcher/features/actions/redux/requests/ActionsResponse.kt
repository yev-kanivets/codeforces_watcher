package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.features.actions.models.Action
import com.google.gson.annotations.SerializedName

data class ActionsResponse(
    val status: String,
    @SerializedName("result") val actions: List<Action>
)