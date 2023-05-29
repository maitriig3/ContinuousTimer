package com.hkngtech.continuoustimer.ui.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.databinding.AdapterTasksBinding

class TasksAdapter(var tasks: List<Tasks>, val onClick: (Int, Tasks) -> Unit) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {


    inner class TasksViewHolder(val binding: AdapterTasksBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            AdapterTasksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        with(holder.binding) {
            val timeUnit = when (tasks[position].timeUnit) {
                "S" -> "secs"
                "M" -> "mins"
                "H" -> "hours"
                "D" -> "days"
                else -> ""
            }
            val task = "${tasks[position].task} - ${tasks[position].time} $timeUnit"
            txtName.text = task
            imgEdit.setOnClickListener {
                onClick(0, tasks[position])
            }
            imgDelete.setOnClickListener {
                onClick(1, tasks[position])
            }
        }
    }

    override fun getItemCount(): Int = tasks.size
}