package com.hkngtech.continuoustimer.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel(){

    var dateFilter = ""
    var addedTimeFilter = false
    var ascFilter = false

    var schedule = ""
    var breakInBetween = ""

    var serviceIsNotRunning = true

    /**
     * To start service after asking for notification permission or battery optimization
     */
    var scheduleIdCountdown = -1

    suspend fun insert(): Int{
        return withContext(viewModelScope.coroutineContext){
            roomRepository.insertSchedule(schedule,breakInBetween)
            roomRepository.getScheduleId()
        }
    }

    fun getAll(): Flow<List<Schedule>> {
        return flow {
            roomRepository.getAllSchedule().collect{ scheduleList ->
                if(dateFilter.isEmpty()){
                    if(ascFilter){
                        emit(scheduleList.sortedBy { if(addedTimeFilter) it.addedTime else it.updatedTime })
                    }else{
                        emit(scheduleList.sortedByDescending { if(addedTimeFilter) it.addedTime else it.updatedTime })
                    }
                }
            }
        }
    }

    suspend fun delete(scheduleId: Int) = withContext(viewModelScope.coroutineContext){roomRepository.deleteSchedule(scheduleId)}


    fun validateSchedule() = if(schedule.isEmpty())
        "Schedule cannot be empty"
    else
        ""

}