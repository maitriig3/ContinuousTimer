package com.hkngtech.continuoustimer.ui.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkngtech.continuoustimer.databinding.FragmentTasksBinding
import com.hkngtech.continuoustimer.ui.addtasks.AddTasksFragment
import com.hkngtech.continuoustimer.ui.base.BaseFragment
import com.hkngtech.continuoustimer.ui.schedule.ScheduleViewModel
import com.hkngtech.continuoustimer.ui.tasks.adapter.TasksAdapter
import com.hkngtech.continuoustimer.utils.editTextValidation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TasksFragment: BaseFragment<FragmentTasksBinding>(FragmentTasksBinding::inflate) {

    val scheduleViewModel by viewModels<ScheduleViewModel>()
    val tasksViewModel by activityViewModels<TasksViewModel>()
    val args : TasksFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recViewTasks.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        tasksViewModel.scheduleId = args.scheduleId
        tasksRefresh()

        binding.btnAdd.setOnClickListener {
            with(scheduleViewModel){
                with(binding){
                    schedule = editTextSchedule.text.toString().trim()
                    breakInBetween = editTextBreakInBetween.text.toString().trim()
                    editTextValidation(editLayoutSchedule,validateSchedule())
                    if(editLayoutSchedule.boxStrokeColor == Color.GREEN){
                        lifecycleScope.launchWhenCreated {
                            tasksViewModel.scheduleId = insert()
                            tasksRefresh()
                        }
                    }
                }
            }
        }

        binding.btnAddTasks.setOnClickListener {
            tasksViewModel.tasksId = -1
            val addTasksFragment = AddTasksFragment()
            addTasksFragment.show(childFragmentManager,"Add Task")
        }
    }

    private fun tasksRefresh(){
        if(tasksViewModel.scheduleId == -1){
            binding.layoutTasks.isVisible = false
            binding.btnAdd.text = "ADD"
        }else{
            binding.layoutTasks.isVisible = true
            binding.btnAdd.text = "EDIT"
            lifecycleScope.launchWhenCreated {
                val schedule = tasksViewModel.getSchedule()
                binding.editTextSchedule.setText(schedule.schedule)
                binding.editTextBreakInBetween.setText(schedule.break_in_between)
                tasksViewModel.getAll().collectLatest{
                    binding.recViewTasks.adapter = TasksAdapter(it){which,tasks ->
                        if(which == 0){
                            tasksViewModel.tasksId = tasks.tasksId
                            val addTasksFragment = AddTasksFragment()
                            addTasksFragment.show(childFragmentManager,"Add Task")
                        }else if(which == 1){
                            lifecycleScope.launchWhenCreated {
                                tasksViewModel.delete(tasks.tasksId)
                            }
                        }
                    }
                }
            }
        }
    }
}