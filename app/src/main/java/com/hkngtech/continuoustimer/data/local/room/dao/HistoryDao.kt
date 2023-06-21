package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hkngtech.continuoustimer.data.local.room.HistorySchedule
import com.hkngtech.continuoustimer.data.local.room.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(history: History)
    @Update
    suspend fun update(history: History)

    @Query("select * from history_table where ended_time =0 order by started_time desc limit 1")
    suspend fun getRecent(): History
    @Query("select h.*,s.schedule from schedule_table as s left join history_table as h on s.schedule_id = h.schedule_id")
    fun getAll(): Flow<List<HistorySchedule>>
}