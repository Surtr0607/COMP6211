package com.example.myapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao{
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from UserTable WHERE id = :key")
    fun get(key: Int): User?

    @Query("DELETE FROM UserTable")
    fun clear()

    @Query("SELECT * FROM UserTable ORDER BY id DESC LIMIT 1")
    fun getFirstname(): User?

    @Query("SELECT * FROM UserTable ORDER BY id DESC")
    fun getAllUser(): LiveData<List<User>>


}

