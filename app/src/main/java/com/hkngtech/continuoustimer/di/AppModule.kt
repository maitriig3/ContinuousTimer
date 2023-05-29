package com.hkngtech.continuoustimer.di

import android.app.Application
import androidx.room.Room
import com.hkngtech.continuoustimer.data.local.room.DatabaseC
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesDatabase(context: Application) = Room.databaseBuilder(context.applicationContext, DatabaseC::class.java,
        "continuousTimerDB.db").build()

    @Provides
    @Singleton
    fun providesRoomRepository(databaseC: DatabaseC) = RoomRepository(databaseC)

}