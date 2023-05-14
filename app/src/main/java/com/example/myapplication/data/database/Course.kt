package com.example.myapplication.data.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CourseTable")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String

)