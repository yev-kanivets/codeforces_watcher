package com.bogdan.codeforceswatcher

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromString(value: String): List<RatingChange> {
        val listType = object : TypeToken<List<RatingChange>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<RatingChange>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}