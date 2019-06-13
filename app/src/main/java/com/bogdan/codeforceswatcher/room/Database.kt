package com.bogdan.codeforceswatcher.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.model.User


@Database(entities = [User::class, Contest::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun contestDao(): ContestDao

}