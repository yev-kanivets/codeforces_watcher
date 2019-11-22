package com.bogdan.codeforceswatcher.features.problems.redux.requests

import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.google.gson.annotations.SerializedName

data class ProblemsResponse(
    val status: String,
    @SerializedName("result") val result: Result
)

data class Result(val problems: List<Problem>)