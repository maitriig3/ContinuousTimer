package com.hkngtech.continuoustimer.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val scheduleId : Int,
    @ColumnInfo(name = "schedule")
    val schedule : String,
    @ColumnInfo(name = "break_in_between")
    val break_in_between : String,
    @ColumnInfo(name = "added_time")
    val addedTime: Long,
    @ColumnInfo(name = "updated_time")
    val updatedTime: Long
)