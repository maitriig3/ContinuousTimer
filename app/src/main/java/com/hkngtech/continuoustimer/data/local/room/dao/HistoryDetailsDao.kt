package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.HistoryDetailsTask
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDetailsDao {

    @Insert
    suspend fun insert(historyDetails: HistoryDetails)

    @Query("select h.*,t.task from tasks_table as t left join history_details_table as h on t.tasks_id = h.task_id where history_id=:historyId")
    fun getAll(historyId: Int): Flow<List<HistoryDetailsTask>>
}