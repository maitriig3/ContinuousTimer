package com.hkngtech.continuoustimer.others

object Constants {

    fun miltotime(mil : Long) : String{
        val seconds = ((mil.toLong() / 1000) % 60).toInt()
        val minutes = ((mil.toLong() / (1000 * 60) % 60)).toInt()
        val hours = ((mil.toLong() / (1000 * 60 * 60) % 24)).toInt()
        return "$hours:$minutes:$seconds"
    }
}