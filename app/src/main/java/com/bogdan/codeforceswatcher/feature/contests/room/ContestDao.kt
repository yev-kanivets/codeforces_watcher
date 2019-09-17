package com.bogdan.codeforceswatcher.feature.contests.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdan.codeforceswatcher.model.Contest

@Dao
interface ContestDao {

    @Query("SELECT * FROM contest")
    fun getUpcomingContests(): List<Contest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contests: List<Contest>)

    @Query("DELETE FROM contest")
    fun deleteAll()
}
