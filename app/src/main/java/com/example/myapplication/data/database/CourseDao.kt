package com.example.myapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(course: Course)

    @Query("SELECT description from CourseTable WHERE name = :name")
    fun getDescription(name: String): Course?

    @Query("SELECT * FROM CourseTable")
    fun getAllCourses(): LiveData<List<Course>>
}
