package com.bogdan.codeforceswatcher.features.problems.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Problem(
        @PrimaryKey(autoGenerate = true) var id: Long,
        val name: String,
        var enName: String,
        var ruName: String,
        val index: String,
        var contestId: Long,
        var contestName: String,
        var contestTime: Long,
        var isFavourite: Boolean = false
) : Serializable