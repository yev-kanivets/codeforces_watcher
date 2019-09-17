package com.bogdan.codeforceswatcher.network.model

import com.bogdan.codeforceswatcher.model.RatingChange

data class RatingChangeResponse(
    val status: String,
    val result: List<RatingChange>
)
