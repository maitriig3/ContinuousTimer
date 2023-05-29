package com.hkngtech.continuoustimer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTData

@Dao
interface TimeTDao {

    @Insert
    suspend fun insert(timeTData: TimeTData)

    @Query("update timetabledata set name=:name where id=:id")
    suspend fun update(name: String,id : Int) : Int

    @Query("select id from timetabledata where name=:name order by id desc limit 1")
    suspend fun getId(name : String) : Int

    @Query("select * from timetabledata")
    suspend fun getName() : List<TimeTData>

    @Query("delete from timetabledata where id=:id")
    suspend fun delete(id : Int) : Int


}