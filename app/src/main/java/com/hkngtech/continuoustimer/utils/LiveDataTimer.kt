package com.hkngtech.continuoustimer.utils

import androidx.lifecycle.MutableLiveData
import com.hkngtech.continuoustimer.data.local.room.TimerUpdate

object LiveDataTimer {

    var timerUpdate = MutableLiveData<TimerUpdate>()

}