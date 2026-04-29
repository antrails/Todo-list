package com.example.todolist

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [User::class, Mission::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun missionDao(): MissionDao
}