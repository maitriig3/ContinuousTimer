package com.hkngtech.continuoustimer.data.local.room.repository

import com.hkngtech.continuoustimer.data.local.room.DatabaseC
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.utils.getTimeInMillis

class RoomRepository(private val databaseC: DatabaseC) {

    suspend fun insertSchedule(schedule: String,breakInBetween: String){
        databaseC.scheduleDao().insert(Schedule(0,schedule,breakInBetween, getTimeInMillis(), getTimeInMillis()))
    }

    suspend fun getScheduleId() = databaseC.scheduleDao().getScheduleInt()

    fun getAllSchedule() = databaseC.scheduleDao().getAll()

    suspend fun deleteSchedule(scheduleId: Int) = databaseC.scheduleDao().delete(scheduleId)

    suspend fun getSchedule(scheduleId: Int) = databaseC.scheduleDao().getSchedule(scheduleId)

    suspend fun insertTasks(scheduleId: Int, task: String, time: String, timeUnit: String){
        databaseC.tasksDao().insert(Tasks(0, scheduleId,task,time,timeUnit, getTimeInMillis(),
            getTimeInMillis()
        ))
    }

    fun getAllTasks(scheduleId: Int) = databaseC.tasksDao().getAll(scheduleId)

    suspend fun getAllTasksService(scheduleId: Int) = databaseC.tasksDao().getAllService(scheduleId)

    suspend fun deleteTask(tasksId: Int) = databaseC.tasksDao().delete(tasksId)

    suspend fun getTask(tasksId: Int) = databaseC.tasksDao().getTask(tasksId)

    suspend fun updateTask(task: String,time: String,timeUnit: String,tasksId: Int) = databaseC.tasksDao().updateTask(task,time,timeUnit,
        getTimeInMillis(),tasksId
    )

    suspend fun insertHistory(scheduleId: Int) = databaseC.historyDao().insert(
        History(0,scheduleId,
            getTimeInMillis(),0
        )
    )

    suspend fun updateHistory(history: History) {
        databaseC.historyDao().update(
            History(history.historyId,history.scheduleId,history.startedTime,
                getTimeInMillis()
            )
        )
    }

    suspend fun getRecent() = databaseC.historyDao().getRecent()

    suspend fun insertHistoryDetails(historyId: Int,scheduleId: Int,taskId: Int,acknowledged: Boolean) = databaseC.historyDetailsDao().insert(
        HistoryDetails(
        0, historyId, scheduleId, taskId, acknowledged, getTimeInMillis()
    )
    )

    fun getHistory() = databaseC.historyDao().getAll()

    fun getHistoryDetails(historyId: Int) = databaseC.historyDetailsDao().getAll(historyId)


}