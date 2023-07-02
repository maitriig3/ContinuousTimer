package com.hkngtech.continuoustimer.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hkngtech.continuoustimer.data.local.room.DatabaseC
import com.hkngtech.continuoustimer.data.local.room.repository.PreferencesRepository
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
    fun providePreferences(context: Application): SharedPreferences {
        val masterKey: MasterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "com.hkngtech.continuoustimer",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideEditor(preferences: SharedPreferences): SharedPreferences.Editor{
        return preferences.edit()
    }
    @Provides
    @Singleton
    fun providesDatabase(context: Application) = Room.databaseBuilder(context.applicationContext, DatabaseC::class.java,
        "continuousTimerDB.db").build()

    @Provides
    @Singleton
    fun providesRoomRepository(databaseC: DatabaseC) = RoomRepository(databaseC)

    @Provides
    @Singleton
    fun providePreferencesRepository(preferences: SharedPreferences, editor: SharedPreferences.Editor)
            = PreferencesRepository( preferences, editor)

}