package com.bogdan.codeforceswatcher.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bogdan.codeforceswatcher.model.Contest
import androidx.room.Delete

@Dao
interface ContestDao {

    @Query("SELECT * FROM contest WHERE phase = 'BEFORE'")
    fun getUpcomingContests(): LiveData<List<Contest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contests: List<Contest>)

    @Delete
    fun deleteAll(contests: List<Contest>)

}