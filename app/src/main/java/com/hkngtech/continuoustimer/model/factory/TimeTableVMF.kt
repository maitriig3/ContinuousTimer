package com.hkngtech.continuoustimer.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM

class TimeTableVMF(val timeTableRepository: TimeTableRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimeTableVM(timeTableRepository) as T
    }
}