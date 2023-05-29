package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(history: History)

    @Query("select * from history_table")
    fun getAll(): Flow<List<History>>
}