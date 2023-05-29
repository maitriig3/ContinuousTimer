package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert
    suspend fun insert(tasks: Tasks)

    @Query("select * from tasks_table where schedule_id=:schedule_id")
    fun getAll(schedule_id: Int): Flow<List<Tasks>>

    @Query("select * from tasks_table where schedule_id=:schedule_id")
    suspend fun getAllService(schedule_id: Int): List<Tasks>

    @Query("delete from tasks_table where tasks_id=:tasksId")
    suspend fun delete(tasksId: Int)

    @Query("select * from tasks_table where tasks_id=:tasksId")
    suspend fun getTask(tasksId: Int): Tasks

    @Query("update tasks_table set task=:task,time=:time,time_unit=:timeUnit,updated_time=:timeMilli where tasks_id=:tasksId")
    suspend fun updateTask(task: String, time: String, timeUnit: String, timeMilli: Long,tasksId: Int): Int
}