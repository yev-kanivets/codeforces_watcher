package com.bogdan.codeforceswatcher.model

data class RatingChange(val contestId: Int, val contestName: String, val handle: String, val rank: Int, val ratingUpdateTimeSeconds: Long, val oldRating: Int, val newRating: Int)