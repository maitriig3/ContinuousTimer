package com.hkngtech.continuoustimer.ui.history

import androidx.lifecycle.ViewModel
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel(){

    var dateFilter = ""
    var addedTimeFilter = false
    var ascFilter = false

    fun getHistory() = flow {
        roomRepository.getHistory().collect{ historyList ->
            if(dateFilter.isEmpty()){
                if(ascFilter){
                    emit(historyList.sortedBy { if(addedTimeFilter) it.startedTime else it.endedTime })
                }else{
                    emit(historyList.sortedByDescending { if(addedTimeFilter) it.startedTime else it.endedTime })
                }
            }
        }
    }


    fun getHistoryDetails(historyId: Int) = flow {
        roomRepository.getHistoryDetails(historyId).collect{ historyDetailsList ->
            if(dateFilter.isEmpty()){
                if(ascFilter){
                    emit(historyDetailsList.sortedBy { it.addedTime})
                }else{
                    emit(historyDetailsList.sortedByDescending {it.addedTime })
                }
            }
        }
    }

}