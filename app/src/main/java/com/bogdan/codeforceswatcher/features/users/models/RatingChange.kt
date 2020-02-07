package com.bogdan.codeforceswatcher.features.users.models

data class RatingChange(
        val contestId: Int,
        val contestName: String,
        val handle: String,
        val rank: Int,
        val ratingUpdateTimeSeconds: Long,
        val oldRating: Int,
        val newRating: Int
)
