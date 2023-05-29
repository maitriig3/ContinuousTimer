package com.hkngtech.continuoustimer.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.room.entity.Schedule
import com.hkngtech.continuoustimer.databinding.AdapterScheduleBinding

class ScheduleAdapter(var schedule: List<Schedule>, val onClick: (Int, Schedule) -> Unit) :
    RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: AdapterScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdapterScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.binding) {
            name.text = schedule[position].schedule
            info.setOnClickListener {
                onClick(0, schedule[position])
            }
            time.setOnClickListener {
                onClick(1, schedule[position])
            }
            delete.setOnClickListener {
                onClick(2, schedule[position])
            }
        }
    }

    override fun getItemCount(): Int = schedule.size
}