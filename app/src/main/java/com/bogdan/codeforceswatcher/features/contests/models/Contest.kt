package com.bogdan.codeforceswatcher.features.contests.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Contest(
        @PrimaryKey val id: Long,
        val name: String,
        @SerializedName("startTimeSeconds") val time: Long,
        @SerializedName("durationSeconds") val duration: Long,
        val phase: String
)
