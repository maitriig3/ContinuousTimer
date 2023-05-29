package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDetailsDao {

    @Insert
    suspend fun insert(historyDetails: HistoryDetails)

    @Query("select * from history_details_table where history_id=:historyId")
    fun getAll(historyId: Int): Flow<List<HistoryDetails>>
}