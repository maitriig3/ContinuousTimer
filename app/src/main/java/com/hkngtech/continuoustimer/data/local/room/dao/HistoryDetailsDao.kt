package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hkngtech.continuoustimer.data.local.room.HistoryDetailsTask
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDetailsDao {

    @Insert
    suspend fun insert(historyDetails: HistoryDetails)

    @Update
    suspend fun update(historyDetails: HistoryDetails)

    @Query("select h.*,t.task from tasks_table as t left outer join history_details_table as h on t.tasks_id = h.task_id and h.history_id =:historyId where t.schedule_id = :scheduleId")
    fun getAll(scheduleId:Int,historyId: Int): Flow<List<HistoryDetailsTask>>



    @Query("select * from history_details_table order by added_time desc limit 1")
    suspend fun getRecent(): HistoryDetails
}