package com.hkngtech.continuoustimer.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.room.HistorySchedule
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.databinding.AdapterHistoryBinding
import com.hkngtech.continuoustimer.databinding.AdapterScheduleBinding
import com.hkngtech.continuoustimer.ui.schedule.adapter.ScheduleAdapter
import com.hkngtech.continuoustimer.utils.Constants
import com.hkngtech.continuoustimer.utils.getDate

class HistoryAdapter(var historySchedule: List<HistorySchedule>, val onClick: (Int, HistorySchedule) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: AdapterHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdapterHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.binding) {
            txtTask.text = historySchedule[position].schedule
            txtStartedTime.text = getDate(historySchedule[position].startedTime,Constants.DATE_DD_MM_YYYY_HH_MM_SS)
            txtEndedTime.text = getDate(historySchedule[position].endedTime,Constants.DATE_DD_MM_YYYY_HH_MM_SS)
            layoutHistory.setOnClickListener {
                onClick(0,historySchedule[position])
            }
        }
    }

    override fun getItemCount(): Int = historySchedule.size
}