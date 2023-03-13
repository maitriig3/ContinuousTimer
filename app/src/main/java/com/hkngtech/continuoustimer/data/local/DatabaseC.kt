package com.hkngtech.continuoustimer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hkngtech.continuoustimer.data.local.dao.TimeTDao
import com.hkngtech.continuoustimer.data.local.dao.TimeTtaskDao
import com.hkngtech.continuoustimer.data.local.entity.TimeTData
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData

@Database(entities = [TimeTData::class, TimeTtaskData::class], version = 1)
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

}