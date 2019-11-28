package com.bogdan.codeforceswatcher.features.problems.room

import androidx.room.*
import com.bogdan.codeforceswatcher.features.problems.models.Problem

@Dao
interface ProblemsDao {

    @Query("SELECT * FROM problem")
    fun getAll(): List<Problem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(problems: List<Problem>): List<Long>

    @Query("DELETE FROM problem")
    fun deleteAll()

    @Delete
    fun delete(problem: Problem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(problem: Problem)
}
