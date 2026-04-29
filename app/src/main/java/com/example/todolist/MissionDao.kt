package com.example.todolist

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface MissionDao {

    @Insert
    suspend fun insertMission(mission: Mission): Long

    @Query("SELECT * FROM missions WHERE userAccount = :account ORDER BY id DESC")
    suspend fun getAllMissionsByUser(account: String): List<Mission>

    @Update
    suspend fun updateMission(mission: Mission)

    @Delete
    suspend fun deleteMission(mission: Mission)

    @Query("SELECT * FROM missions WHERE userAccount = :account AND date = :date ORDER BY id DESC")
    suspend fun getMissionsByUserAndDate(account: String, date: String): List<Mission>
}