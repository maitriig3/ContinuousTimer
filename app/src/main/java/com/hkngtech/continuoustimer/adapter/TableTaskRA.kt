package com.hkngtech.continuoustimer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData
import com.hkngtech.continuoustimer.databinding.TabletaskdisplayBinding

class TableTaskRA(var timeTtaskData: ArrayList<TimeTtaskData>, val onClick: OnClick) : RecyclerView.Adapter<TableTaskRA.MyViewHolder>(){


    inner class MyViewHolder(val binding : TabletaskdisplayBinding) : RecyclerView.ViewHolder(binding.root){
        var pos = 0

        init {

        }
    }

    interface OnClick{
        fun onclick(timeTtaskData: TimeTtaskData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(TabletaskdisplayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.task.text = timeTtaskData[position].task
        holder.binding.time.text = timeTtaskData[position].time
        holder.pos = position
    }

    override fun getItemCount(): Int = timeTtaskData.size

}