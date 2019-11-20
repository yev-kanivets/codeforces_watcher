package com.bogdan.codeforceswatcher.features.problems.models

data class Problem(
    val name: String,
    var enName: String?,
    var ruName: String?,
    val index: String,
    val contestId: Int?
)