package com.hkngtech.continuoustimer.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.DatabaseC
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData
import com.hkngtech.continuoustimer.databinding.AddTaskDisplayBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTaskRA(var tableid :Int, var timeTtaskData: ArrayList<TimeTtaskData>, val onClick: OnClick, val context: Context) : RecyclerView.Adapter<AddTaskRA.MyViewHolder>(){

    var notifying = false
    var notfirst = false
    val databaseC = DatabaseC.getDatabase(context)

    inner class MyViewHolder(val binding : AddTaskDisplayBinding) : RecyclerView.ViewHolder(binding.root){
        var pos = 0

        init {
            binding.task.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(!notifying) {
                        val task = binding.task.text.toString().trim()
                        timeTtaskData[pos].task = task
                        GlobalScope.launch(Dispatchers.IO) {
                            if(timeTtaskData[pos].id == 0){
                                databaseC.timeTtaskDao().insert(TimeTtaskData(0,tableid,task,""))
                                timeTtaskData[pos].id = databaseC.timeTtaskDao().getId(task)
                            }else{
                                databaseC.timeTtaskDao().updateTask(task,timeTtaskData[pos].id)
                            }
                        }
                    }
                }
            })
            binding.time.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(!notifying)
                        timeTtaskData[pos].time = binding.time.text.toString().trim()
                    if(timeTtaskData[pos].id == 0){
                        //What to do if time is added first
                    }else{
                        GlobalScope.launch(Dispatchers.IO) {
                            databaseC.timeTtaskDao().updateTime(timeTtaskData[pos].time,timeTtaskData[pos].id)
                        }
                    }
                }
            })
            binding.newtask.setOnClickListener {
                timeTtaskData.add(TimeTtaskData(0,tableid,"",""))
                notifying = true
                notfirst = true
                notifyDataSetChanged()
            }
        }
    }

    interface OnClick{
        fun onclick(timeTtaskData: TimeTtaskData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AddTaskDisplayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        notifying = true
        holder.binding.task.setText(timeTtaskData[position].task)
        holder.binding.time.setText(timeTtaskData[position].time)
        holder.binding.newtask.isVisible = position == timeTtaskData.size-1
        holder.pos = position
        notifying = false
        if(notfirst){
            if(position == timeTtaskData.size-1)
                holder.binding.task.requestFocus()
        }
    }

    override fun getItemCount(): Int = timeTtaskData.size

}