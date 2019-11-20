package com.bogdan.codeforceswatcher.features.users.room

import androidx.room.*
import com.bogdan.codeforceswatcher.features.users.models.User

@Dao
interface UsersDao {

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Long): User

    @Insert
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}
