package com.example.todolist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist.User


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE account = :account AND password = :password LIMIT 1")
    suspend fun login(account: String,password: String): User?

    @Query("SELECT * FROM users WHERE account = :account LIMIT 1")
    suspend fun getUserByAccount(account: String): User?

    @Query ("UPDATE users SET password = :password WHERE account = :account ")
    suspend fun upDatePassword(account: String,password: String): Int
}