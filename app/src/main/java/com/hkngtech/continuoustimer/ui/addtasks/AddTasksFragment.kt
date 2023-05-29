package com.hkngtech.continuoustimer.ui.addtasks

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.databinding.DialogAddTaskBinding
import com.hkngtech.continuoustimer.ui.tasks.TasksViewModel
import com.hkngtech.continuoustimer.utils.editTextValidation

class AddTasksFragment : DialogFragment() {

    private val tasksViewModel by activityViewModels<TasksViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogAddTaskBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(binding.root).create()

        with(binding){
            editTextTimeUnit.setAdapter(ArrayAdapter(requireContext(), androidx.appcompat.R.layout.select_dialog_item_material,resources.getStringArray(R.array.time_unit)))

            if(tasksViewModel.tasksId != -1){
                btnAdd.text = "EDIT"
                lifecycleScope.launchWhenCreated {
                    binding.editTextTask.isEnabled = false
                    binding.editTextTime.isEnabled = false
                    binding.editTextTimeUnit.isEnabled = false
                    val tasks = tasksViewModel.getTask()
                    editTextTask.setText(tasks.task)
                    editTextTime.setText(tasks.time)
                    editTextTimeUnit.setText(tasks.timeUnit)
                    binding.editTextTask.isEnabled = true
                    binding.editTextTime.isEnabled = true
                    binding.editTextTimeUnit.isEnabled = true
                }
            }else
                btnAdd.text = "ADD"

            imgClose.setOnClickListener {
                alertDialog.dismiss()
            }

            btnAdd.setOnClickListener {
                with(tasksViewModel){
                    tasks = editTextTask.text.toString().trim()
                    time = editTextTime.text.toString().trim()
                    timeUnit = editTextTimeUnit.text.toString().trim()
                    editTextValidation(editLayoutTask,validateTasks())
                    editTextValidation(editLayoutTime,validateTime())
                    if(editLayoutTask.boxStrokeColor == Color.GREEN && editLayoutTime.boxStrokeColor == Color.GREEN){
                        lifecycleScope.launchWhenCreated {
                            if(tasksViewModel.tasksId != -1){
                                updateTask()
                                alertDialog.dismiss()
                            }else{
                                insert()
                                editTextTask.setText("")
                                editTextTime.setText("")
                            }
                        }
                    }
                }
            }
        }
        return alertDialog
    }

}