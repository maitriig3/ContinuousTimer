package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTtaskData

@Dao
interface TimeTtaskDao {

    @Insert
    suspend fun insert(timeTtaskData: TimeTtaskData)

    @Query("select * from timettaskdata where tableid = :id")
    suspend fun getTask(id : Int) : List<TimeTtaskData>

    @Query("select id from timettaskdata where task=:name order by id desc limit 1")
    suspend fun getId(name : String) : Int

    @Query("update timettaskdata set task=:task where id=:id")
    suspend fun updateTask(task : String,id : Int) :Int

    @Query("update timettaskdata set time=:time where id=:id")
    suspend fun updateTime(time : String,id : Int) :Int

    @Query("delete from timettaskdata where tableid=:id")
    suspend fun delete(id : Int) : Int
}