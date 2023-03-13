package com.hkngtech.continuoustimer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.entity.TimeTData
import com.hkngtech.continuoustimer.databinding.TablenamedisplayBinding

class TableNameRA(var timeTData: ArrayList<TimeTData>, val onClick: OnClick) : RecyclerView.Adapter<TableNameRA.MyViewHolder>(){


    inner class MyViewHolder(val binding : TablenamedisplayBinding) : RecyclerView.ViewHolder(binding.root){
        var pos = 0

        init {
            binding.info.setOnClickListener {
                onClick.onclick(0,timeTData[pos])
            }
            binding.time.setOnClickListener {
                onClick.onclick(1,timeTData[pos])
            }
            binding.delete.setOnClickListener {
                onClick.onclick(2,timeTData[pos])
            }
        }
    }

    interface OnClick{
        fun onclick(which : Int,timeTData: TimeTData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(TablenamedisplayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text = timeTData[position].name
        holder.pos = position
    }

    override fun getItemCount(): Int = timeTData.size
}