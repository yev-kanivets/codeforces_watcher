package com.bogdan.codeforceswatcher.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bogdan.codeforceswatcher.features.contests.room.ContestsDao
import com.bogdan.codeforceswatcher.features.users.room.UsersDao
import com.bogdan.codeforceswatcher.features.contests.models.Contest
import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.features.problems.room.ProblemsDao
import com.bogdan.codeforceswatcher.features.users.models.User

@Database(entities = [User::class, Contest::class, Problem::class],
    version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    abstract fun contestsDao(): ContestsDao

    abstract fun problemsDao(): ProblemsDao
}
