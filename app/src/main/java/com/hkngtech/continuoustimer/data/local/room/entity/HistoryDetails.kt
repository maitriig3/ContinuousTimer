package com.hkngtech.continuoustimer.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_details_table")
data class HistoryDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("history_detail_id")
    val historyDetailId: Int,
    @ColumnInfo("history_id")
    val historyId: Int,
    @ColumnInfo("schedule_id")
    val scheduleId: Int,
    @ColumnInfo("task_id")
    val taskId: Int,
    @ColumnInfo("acknowledged")
    val acknowledged: Boolean,
    @ColumnInfo(name = "added_time")
    val addedTime: Long

)
