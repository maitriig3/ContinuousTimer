package com.hkngtech.continuoustimer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timettaskdata")
data class TimeTtaskData (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val tableid : Int,
    var task : String,
    var time : String
)