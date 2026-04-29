package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class Mission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String="",
    val context: String="",
    val date: String="",
    val time: String="",
    val location: String="",
    val isDone: Boolean=false,
    val userAccount: String=""
)
