package com.bogdan.codeforceswatcher.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.model.User

@Database(entities = [User::class, Contest::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun contestDao(): ContestDao

}