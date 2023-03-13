package com.hkngtech.continuoustimer.ttfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData
import com.hkngtech.continuoustimer.databinding.FragmentTableTaskBinding
import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM

class TableTask : Fragment() {

    lateinit var binding : FragmentTableTaskBinding
    lateinit var timeTableVM: TimeTableVM
    lateinit var context1 : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_table_task, container, false)
        binding = FragmentTableTaskBinding.bind(view)
        context1 = requireContext()
        timeTableVM = ViewModelProvider(requireActivity(), TimeTableVMF(
            TimeTableRepository(context1)
        )
        )[TimeTableVM::class.java]

        binding.add.setOnClickListener {
            val task = binding.task.text.toString().trim()
            val time = binding.time.text.toString().trim()
            if(task.isNotEmpty() && time.isNotEmpty()){
                lifecycleScope.launchWhenCreated {
                    timeTableVM.timeTableRepository.insertTask(
                        TimeTtaskData(0,
                            timeTableVM.tableid,task,time)
                    )
                    clearData()
                }
            }
        }

        return view
    }

    fun clearData(){
        binding.task.setText("")
        binding.time.setText("")
    }


}