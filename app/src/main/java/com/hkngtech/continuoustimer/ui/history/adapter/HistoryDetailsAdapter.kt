package com.hkngtech.continuoustimer.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.data.local.room.HistoryDetailsTask
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.HistoryDetails
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.databinding.AdapterHistoryBinding
import com.hkngtech.continuoustimer.databinding.AdapterHistoryDetailsBinding
import com.hkngtech.continuoustimer.databinding.AdapterScheduleBinding
import com.hkngtech.continuoustimer.ui.schedule.adapter.ScheduleAdapter
import com.hkngtech.continuoustimer.utils.Constants
import com.hkngtech.continuoustimer.utils.getDate

class HistoryDetailsAdapter(var historyDetailsTask: List<HistoryDetailsTask>, val onClick: (Int, HistoryDetailsTask) -> Unit) :
    RecyclerView.Adapter<HistoryDetailsAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: AdapterHistoryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdapterHistoryDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.binding) {
            txtTask.text = historyDetailsTask[position].task
            imgApp.setImageDrawable(ResourcesCompat.getDrawable(root.resources, R.drawable.ic_timer_alive,null
            ))
            imgUser.setImageDrawable(ResourcesCompat.getDrawable(root.resources, R.drawable.ic_user_alive,null
            ))
        }
    }

    override fun getItemCount(): Int = historyDetailsTask.size
}