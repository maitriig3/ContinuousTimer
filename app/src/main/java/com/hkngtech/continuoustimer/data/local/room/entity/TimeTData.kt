package com.hkngtech.continuoustimer.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetabledata")
data class TimeTData (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String
)