package com.bogdan.codeforceswatcher.feature.contests

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdan.codeforceswatcher.model.Contest

@Dao
interface ContestDao {

    @Query("SELECT * FROM contest WHERE phase = 'BEFORE'")
    fun getUpcomingContests(): LiveData<List<Contest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contests: List<Contest>)

    @Delete
    fun deleteAll(contests: List<Contest>)
}
