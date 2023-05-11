package com.example.myapplication.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val firstname: String,
    @ColumnInfo  val lastname: String
)