package com.bogdan.codeforceswatcher.network.model

import com.bogdan.codeforceswatcher.model.Contest

data class ContestResponse(
    val status: String,
    val result: List<Contest>
)
