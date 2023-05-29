package com.hkngtech.continuoustimer.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tasks_id")
    val tasksId : Int,
    @ColumnInfo(name = "schedule_id")
    val scheduleId : Int,
    @ColumnInfo(name = "task")
    val task : String,
    @ColumnInfo(name = "time")
    val time : String,
    @ColumnInfo(name = "time_unit")
    val timeUnit : String,
    @ColumnInfo(name = "added_time")
    val addedTime: Long,
    @ColumnInfo(name = "updated_time")
    val updatedTime: Long
)