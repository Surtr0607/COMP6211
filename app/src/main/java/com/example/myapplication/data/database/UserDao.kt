package com.example.myapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

