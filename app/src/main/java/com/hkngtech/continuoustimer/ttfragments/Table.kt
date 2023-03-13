package com.hkngtech.continuoustimer.ttfragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.adapter.AddTaskRA
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData
import com.hkngtech.continuoustimer.databinding.FragmentTableBinding
import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM

class Table : Fragment(), AddTaskRA.OnClick {
    lateinit var binding : FragmentTableBinding
    lateinit var timeTableVM: TimeTableVM
    lateinit var context1 : Context
    lateinit var addTaskRA: AddTaskRA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_table, container, false)
        binding = FragmentTableBinding.bind(view)
        context1 = requireContext()
        timeTableVM = ViewModelProvider(requireActivity(), TimeTableVMF(
            TimeTableRepository(context1)
        )
        )[TimeTableVM::class.java]

        addTaskRA = AddTaskRA(timeTableVM.tableid, ArrayList(),this,context1)

        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launchWhenCreated {
                    timeTableVM.tablename = binding.name.text.toString().trim()
                    if (timeTableVM.tableid == 0){
                        timeTableVM.insertTableName()
                        binding.recview.apply {
                            addTaskRA.timeTtaskData = ArrayList<TimeTtaskData>().apply {add(TimeTtaskData(0,timeTableVM.tableid,"",""))}
                            addTaskRA.tableid = timeTableVM.tableid
                            adapter = addTaskRA
                            layoutManager = GridLayoutManager(context1,1,
                                GridLayoutManager.VERTICAL,false)
                        }
                    }else{
                        timeTableVM.updateTableName()
                    }
                }


            }

        })

        return view
    }

    override fun onclick(timeTtaskData: TimeTtaskData) {
    }

}