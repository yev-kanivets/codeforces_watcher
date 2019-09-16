package com.bogdan.codeforceswatcher.feature.contests

import com.bogdan.codeforceswatcher.model.Contest

data class ContestResponse(
    val status: String,
    val result: List<Contest>
)
