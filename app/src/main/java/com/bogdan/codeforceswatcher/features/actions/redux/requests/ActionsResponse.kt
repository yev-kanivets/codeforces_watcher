package com.bogdan.codeforceswatcher.features.actions.redux.requests

import com.bogdan.codeforceswatcher.features.actions.models.CFAction
import com.google.gson.annotations.SerializedName

data class ActionsResponse(
    val status: String,
    @SerializedName("result") val actions: List<CFAction>
)