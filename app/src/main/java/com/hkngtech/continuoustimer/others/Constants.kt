package com.hkngtech.continuoustimer.others

object Constants {

    fun miltotime(mil : Long) : String{
        val seconds = ((mil / 1000) % 60).toInt()
        val minutes = ((mil / (1000 * 60) % 60)).toInt()
        val hours = ((mil / (1000 * 60 * 60) % 24)).toInt()
        return "$hours:$minutes:$seconds"
    }
}