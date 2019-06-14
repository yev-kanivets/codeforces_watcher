package com.bogdan.codeforceswatcher.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
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


