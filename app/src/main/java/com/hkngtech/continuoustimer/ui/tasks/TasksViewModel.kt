package com.hkngtech.continuoustimer.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel() {

    var dateFilter = ""
    var addedTimeFilter = false
    var ascFilter = false

    var tasks = ""
    var time = ""
    var timeUnit = "M"

    /**
     * Stored from navArgs from ScheduleFragment to TasksFragment
     */
    var scheduleId = -1

    /**
     * Reference from TasksFragment to AddTasksFragment
     */
    var tasksId = -1

    suspend fun insert(){
        withContext(viewModelScope.coroutineContext){
            roomRepository.insertTasks(scheduleId,tasks,time,timeUnit)
        }
    }

    fun getAll(): Flow<List<Tasks>> {
        return flow {
            roomRepository.getAllTasks(scheduleId).collect{ tasksList ->
                if(dateFilter.isEmpty()){
                    if(ascFilter){
                        emit(tasksList.sortedBy { if(addedTimeFilter) it.addedTime else it.updatedTime })
                    }else{
                        emit(tasksList.sortedByDescending { if(addedTimeFilter) it.addedTime else it.updatedTime })
                    }
                }
            }
        }
    }

    suspend fun delete(tasksId: Int) = withContext(viewModelScope.coroutineContext){roomRepository.deleteTask(tasksId)}

    suspend fun getTask() = withContext(viewModelScope.coroutineContext){roomRepository.getTask(tasksId)}

    suspend fun getSchedule() = withContext(viewModelScope.coroutineContext){roomRepository.getSchedule(scheduleId)}

    suspend fun updateTask() = withContext(viewModelScope.coroutineContext){roomRepository.updateTask(tasks,time,timeUnit, tasksId)}

    fun validateTasks() = if(tasks.isEmpty())
        "Task cannot be empty"
    else
        ""

    fun validateTime() = if(time.isEmpty())
        "Task cannot be empty"
    else
        ""


}