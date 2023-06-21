package com.hkngtech.continuoustimer.data.local.room

import androidx.room.ColumnInfo

data class HistoryDetailsTask(
    @ColumnInfo("history_detail_id")
    val historyDetailId: Int,
    @ColumnInfo("history_id")
    val historyId: Int,
    @ColumnInfo("schedule_id")
    val scheduleId: Int,
    @ColumnInfo("task_id")
    val taskId: Int,
    val task: String,
    val acknowledged: Boolean,
    @ColumnInfo("added_time")
    val addedTime: Long
)
