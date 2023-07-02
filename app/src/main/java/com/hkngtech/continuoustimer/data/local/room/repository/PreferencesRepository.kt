package com.hkngtech.continuoustimer.data.local.room.repository

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class PreferencesRepository(private val preferences: SharedPreferences, private val editor: Editor) {



    fun setString(key: String, value: String) {
        with(editor){
            putString(key,value)
            apply()
        }
    }

    fun getString(key: String) = preferences.getString(key,"")

    fun setInt(key: String, value: Int) {
        with(editor){
            putInt(key,value)
            apply()
        }
    }

    fun getInt(key: String) = preferences.getInt(key,-1)

    fun setBoolean(key: String, value: Boolean) {
        with(editor){
            putBoolean(key,value)
            apply()
        }
    }

    fun getBoolean(key: String) = preferences.getBoolean(key,false)
}