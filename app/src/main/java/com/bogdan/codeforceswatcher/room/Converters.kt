package com.bogdan.codeforceswatcher.room

import androidx.room.TypeConverter
import com.bogdan.codeforceswatcher.model.RatingChange
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(value: String): List<RatingChange> {
        val listType = object : TypeToken<List<RatingChange>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<RatingChange>): String {
        return Gson().toJson(list)
    }

}
