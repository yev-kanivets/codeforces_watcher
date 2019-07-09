package com.bogdan.codeforceswatcher.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bogdan.codeforceswatcher.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllLive(): LiveData<List<User>>

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Long): User

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

}


