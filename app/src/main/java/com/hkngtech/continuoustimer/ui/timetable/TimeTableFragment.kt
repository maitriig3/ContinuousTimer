package com.hkngtech.continuoustimer.ui.timetable

//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import androidx.recyclerview.widget.GridLayoutManager
//import com.hkngtech.continuoustimer.ui.countdown.CountDownService
//import com.hkngtech.continuoustimer.R
//import com.hkngtech.continuoustimer.ui.dashboard.adapter.ScheduleAdapter
//import com.hkngtech.continuoustimer.data.local.room.entity.TimeTData
//import com.hkngtech.continuoustimer.databinding.FragmentTimeTableBinding
//import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
//import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
//import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM
//import com.hkngtech.continuoustimer.others.Constants
//import com.hkngtech.continuoustimer.ttfragments.Table
//import com.hkngtech.continuoustimer.ttfragments.TableName
//import com.hkngtech.continuoustimer.ttfragments.TableTask
//import com.hkngtech.continuoustimer.ttfragments.ViewTimeTable
//import com.hkngtech.continuoustimer.ui.base.BaseFragment
//import java.util.ArrayList
//
//class TimeTableFragment : BaseFragment<FragmentTimeTableBinding>(FragmentTimeTableBinding::inflate), TableName.AddTask,
//    ScheduleAdapter.OnClick {
//
//    lateinit var timeTableVM: TimeTableVM
//    var tableName = TableName()
//    var tableTask = TableTask()
//    var viewTimeTable = ViewTimeTable()
//    var table = Table()
//    lateinit var receiver: BroadcastReceiver
//    lateinit var scheduleAdapter : ScheduleAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        timeTableVM = ViewModelProvider(this, TimeTableVMF(
//            TimeTableRepository(requireContext())
//        )
//        )[TimeTableVM::class.java]
//        createNotificationChannel()
//
//        receiver = object : BroadcastReceiver(){
//            override fun onReceive(p0: Context?, p1: Intent?) {
//                Log.e("HERE","IN RECEIVER")
//                if (p1 != null) {
//                    val time = p1.getStringExtra("time").toString()
//
//                    binding.time.text = Constants.miltotime(time.toLong())
////                    val ms = String.format(Locale.US,
////                        "%02d:%02d:%02d", time / 3600,
////                        time % 3600 / 60, time % 60
////                    )
//                }
//            }
//        }
//
//
//
//
//
//        binding.recview.apply {
//            scheduleAdapter = ScheduleAdapter(ArrayList(),this@TimeTableFragment)
//            adapter = scheduleAdapter
//            layoutManager = GridLayoutManager(requireContext(),1, GridLayoutManager.VERTICAL,false)
//        }
//
//        binding.newtable.setOnClickListener {
//            table = Table()
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.add(R.id.frame,table)
//            transaction.addToBackStack("name")
//            transaction.commit()
//        }
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        Log.e("HERE","RESUME")
//
//        lifecycleScope.launchWhenCreated {
//            scheduleAdapter.timeTData = timeTableVM.timeTableRepository.getName()
//            scheduleAdapter.notifyDataSetChanged()
//        }
//
//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(IntentFilter("com.hkngtech.timer.countdown")))
//    }
//
//    override fun onPause() {
//        super.onPause()
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//    }
//
//    override fun addtask() {
//        parentFragmentManager.popBackStack()
//        tableTask = TableTask()
//        val transaction = parentFragmentManager.beginTransaction()
//        transaction.replace(R.id.frame,tableTask)
//        transaction.addToBackStack("task")
//        transaction.commit()
//    }
//
//    override fun onclick(which : Int,timeTData: TimeTData) {
//        if(which == 0){
//            viewTimeTable = ViewTimeTable()
//            val bundle = Bundle()
//            bundle.putInt("tableid",timeTData.id)
//            bundle.putString("name",timeTData.name)
//            viewTimeTable.arguments = bundle
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.frame,viewTimeTable)
//            transaction.addToBackStack("view")
//            transaction.commit()
//        }else if(which == 1){
//            Intent(requireContext(), CountDownService::class.java).apply {
//                putExtra("tableid",timeTData.id)
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { TODO
////                    startForegroundService(requireContext())
////                }else
////                    startService(this)
//            }
//        }else if(which == 2){
//            lifecycleScope.launchWhenCreated {
//                val res = timeTableVM.delete(timeTData.id)
//                lifecycleScope.launchWhenCreated {
//                    scheduleAdapter.timeTData = timeTableVM.timeTableRepository.getName()
//                    scheduleAdapter.notifyDataSetChanged()
//                }
//            }
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(getString(R.string.channel_id), name, importance).apply {
//                description = descriptionText
//            }
////            val notificationManager: NotificationManager = TODO
////                getSystemService(requireContext(),Context.NOTIFICATION_SERVICE) as NotificationManager
////            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//}