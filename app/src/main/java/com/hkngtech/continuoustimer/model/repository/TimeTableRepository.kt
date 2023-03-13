package com.hkngtech.continuoustimer.model.repository

import android.content.Context
import android.util.Log
import com.hkngtech.continuoustimer.data.local.DatabaseC
import com.hkngtech.continuoustimer.data.local.entity.TimeTData
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData

class TimeTableRepository(val context : Context) {

    val databaseC = DatabaseC.getDatabase(context)

    suspend fun insertTableName(timeTData : TimeTData){
        val res = databaseC.timeTDao().insert(timeTData)
        Log.e("INSERT",res.toString())
    }

    suspend fun updateTableName(name :String,id: Int) :Int{
        return databaseC.timeTDao().update(name, id)
    }

    suspend fun getId(name : String) : Int{
        val id = databaseC.timeTDao().getId(name)
        Log.e("ID",id.toString())
        return id
    }

    suspend fun insertTask(timeTtaskData: TimeTtaskData){
        databaseC.timeTtaskDao().insert(timeTtaskData)
    }

    suspend fun getName() : ArrayList<TimeTData>{
        return databaseC.timeTDao().getName() as ArrayList<TimeTData>
    }

    suspend fun getTask(id : Int) : ArrayList<TimeTtaskData>{
        return databaseC.timeTtaskDao().getTask(id) as ArrayList<TimeTtaskData>
    }

    suspend fun delete(id : Int) : Int{
        databaseC.timeTtaskDao().delete(id)
        return databaseC.timeTDao().delete(id)
    }


}