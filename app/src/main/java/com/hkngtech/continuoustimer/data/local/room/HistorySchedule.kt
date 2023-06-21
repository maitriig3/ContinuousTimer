package com.hkngtech.continuoustimer.data.local.room

import androidx.room.ColumnInfo

data class HistorySchedule(
    @ColumnInfo("history_id")
    val historyId: Int,
    @ColumnInfo("schedule_id")
    val scheduleId: Int,
    val schedule: String,
    @ColumnInfo("started_time")
    val startedTime: Long,
    @ColumnInfo("ended_time")
    val endedTime: Long
)