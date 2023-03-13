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
import com.hkngtech.continuoustimer.databinding.FragmentTableNameBinding
import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM

class TableName : Fragment() {

    lateinit var binding : FragmentTableNameBinding
    lateinit var timeTableVM: TimeTableVM
    lateinit var context1 : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_table_name, container, false)
        binding = FragmentTableNameBinding.bind(view)
        context1 = requireContext()
        timeTableVM = ViewModelProvider(requireActivity(), TimeTableVMF(
            TimeTableRepository(context1)
        )
        )[TimeTableVM::class.java]

        binding.addtask.setOnClickListener {
            val name = binding.name.text.toString().trim()
            if(name.isNotEmpty()){
                lifecycleScope.launchWhenCreated {
                    timeTableVM.tablename = name
                    timeTableVM.insertTableName()
                    val addTask = context1 as AddTask
                    addTask.addtask()
                }

            }
        }


        return view
    }

    interface AddTask{
        fun addtask()
    }


}