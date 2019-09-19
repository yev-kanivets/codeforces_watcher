package com.bogdan.codeforceswatcher.feature.contests.redux.request

import com.bogdan.codeforceswatcher.model.Contest

data class ContestsResponse(
    val status: String,
    val result: List<Contest>
)
