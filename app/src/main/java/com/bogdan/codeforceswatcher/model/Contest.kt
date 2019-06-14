package com.bogdan.codeforceswatcher.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Contest(@PrimaryKey val id: Long, val name: String, @SerializedName("startTimeSeconds") val time: Long, val phase: String)

