package com.bogdan.codeforceswatcher.features.problems.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdan.codeforceswatcher.features.problems.models.Problem

@Dao
interface ProblemsDao {

    @Query("SELECT * FROM problem")
    fun getAll(): List<Problem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(problems: List<Problem>)

    @Query("DELETE FROM problem")
    fun deleteAll()
}
