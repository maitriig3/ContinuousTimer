package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    suspend fun insert(schedule: Schedule)

    @Query("select * from schedule_table")
    fun getAll(): Flow<List<Schedule>>

    @Query("select schedule_id from schedule_table order by added_time desc limit 1")
    suspend fun getScheduleInt(): Int

    @Query("delete from schedule_table where schedule_id=:scheduleId")
    suspend fun delete(scheduleId: Int)

    @Query("select * from schedule_table where schedule_id=:scheduleId")
    suspend fun getSchedule(scheduleId: Int): Schedule

}