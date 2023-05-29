package com.hkngtech.continuoustimer.ttfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.adapter.TableTaskRA
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTtaskData
import com.hkngtech.continuoustimer.databinding.FragmentViewTimeTableBinding
import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM

class ViewTimeTable : Fragment(), TableTaskRA.OnClick {
    lateinit var binding : FragmentViewTimeTableBinding
    lateinit var context1 : Context
    lateinit var timeTableVM: TimeTableVM
    var tableid = 0
    var name = ""
    lateinit var tableTaskRA: TableTaskRA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_time_table, container, false)
        binding = FragmentViewTimeTableBinding.bind(view)
        context1 = requireContext()
        timeTableVM = ViewModelProvider(requireActivity(), TimeTableVMF(
            TimeTableRepository(context1)
        )
        )[TimeTableVM::class.java]

        tableid = this.requireArguments().getInt("tableid")
        name = this.requireArguments().getString("name").toString()

        binding.name.text = name

        binding.recview.apply {
            lifecycleScope.launchWhenCreated {
                tableTaskRA = TableTaskRA(timeTableVM.timeTableRepository.getTask(tableid),this@ViewTimeTable)
                adapter = tableTaskRA
                layoutManager = GridLayoutManager(context1,1, GridLayoutManager.VERTICAL,false)
            }

        }


        return view
    }

    override fun onclick(timeTtaskData: TimeTtaskData) {

    }


}