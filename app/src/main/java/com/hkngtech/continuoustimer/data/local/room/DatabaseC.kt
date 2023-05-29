package com.hkngtech.continuoustimer.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hkngtech.continuoustimer.data.local.room.dao.HistoryDao
import com.hkngtech.continuoustimer.data.local.room.dao.HistoryDetailsDao
import com.hkngtech.continuoustimer.data.local.room.dao.ScheduleDao
import com.hkngtech.continuoustimer.data.local.room.dao.TasksDao
import com.hkngtech.continuoustimer.data.local.room.dao.TimeTDao
import com.hkngtech.continuoustimer.data.local.room.dao.TimeTtaskDao
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTData
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTtaskData

@Database(entities = [TimeTData::class, TimeTtaskData::class,Schedule::class,Tasks::class,History::class,HistoryDetails::class], version = 1)
abstract class DatabaseC : RoomDatabase() {

    companion object{
        @Volatile
        var databaseC : DatabaseC? = null

        fun getDatabase(context : Context) : DatabaseC {
            if(databaseC == null){
                synchronized(this){
                    databaseC = Room.databaseBuilder(context.applicationContext, DatabaseC::class.java,
                        "timerDB").build()
                }
            }
            return databaseC!!
        }
    }

    abstract fun timeTDao() : TimeTDao
    abstract fun timeTtaskDao() : TimeTtaskDao
    abstract fun scheduleDao() : ScheduleDao
    abstract fun tasksDao() : TasksDao
    abstract fun historyDao() : HistoryDao
    abstract fun historyDetailsDao() : HistoryDetailsDao

}