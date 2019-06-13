package com.bogdan.codeforceswatcher.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.bogdan.codeforceswatcher.model.Contest
import android.arch.persistence.room.Delete


@Dao
interface ContestDao {

    @Query("SELECT * FROM contest")
    fun getAllLive(): LiveData<List<Contest>>

    @Query("SELECT * FROM contest WHERE phase = 'BEFORE'")
    fun getAllLiveWithBefore(): LiveData<List<Contest>>

    @Query("SELECT * FROM contest")
    fun getAll(): List<Contest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contests: List<Contest>)

    @Delete
    fun deleteAll(contests: List<Contest>)

}