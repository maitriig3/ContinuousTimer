package com.hkngtech.continuoustimer.model.viewmodel

import androidx.lifecycle.ViewModel
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTData
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository

class TimeTableVM(val timeTableRepository: TimeTableRepository) : ViewModel() {

    var tableid = 0
    var tablename = ""

    suspend fun insertTableName(){
        timeTableRepository.insertTableName(TimeTData(0,tablename))
        tableid = timeTableRepository.getId(tablename)
    }

    suspend fun updateTableName() : Int{
        return timeTableRepository.updateTableName(tablename,tableid)
    }

    suspend fun delete(id :Int) :Int{
        return timeTableRepository.delete(id)
    }


}