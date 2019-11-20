package com.bogdan.codeforceswatcher.features.problems.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Problem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    var enName: String?,
    var ruName: String?,
    val index: String,
    val contestId: Int?
)