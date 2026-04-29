package com.example.todolist

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (
    tableName = "users",
    indices = [Index(value=["account"],unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val account : String="",
    val password : String="",
    val securityAnswer: String=""
)
