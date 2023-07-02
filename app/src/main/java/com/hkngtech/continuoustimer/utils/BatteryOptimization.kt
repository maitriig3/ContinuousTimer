package com.hkngtech.continuoustimer.utils

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings

class BatteryOptimization(val context: Context) {


    fun isBatteryOptimizationIgnored(): Boolean{
        val packageName: String = context.packageName
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }

    fun goToBatteryOptimizationSettings(){
        if(!isBatteryOptimizationIgnored()){
            val intent = Intent()
            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            context.startActivity(intent)
        }
    }

}