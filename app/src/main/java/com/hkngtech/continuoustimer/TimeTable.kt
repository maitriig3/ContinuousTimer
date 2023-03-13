package com.hkngtech.continuoustimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.hkngtech.continuoustimer.adapter.TableNameRA
import com.hkngtech.continuoustimer.data.local.entity.TimeTData
import com.hkngtech.continuoustimer.databinding.ActivityTimeTableBinding
import com.hkngtech.continuoustimer.model.factory.TimeTableVMF
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.model.viewmodel.TimeTableVM
import com.hkngtech.continuoustimer.others.Constants
import com.hkngtech.continuoustimer.ttfragments.Table
import com.hkngtech.continuoustimer.ttfragments.TableName
import com.hkngtech.continuoustimer.ttfragments.TableTask
import com.hkngtech.continuoustimer.ttfragments.ViewTimeTable
import java.util.ArrayList

class TimeTable : AppCompatActivity(), TableName.AddTask,TableNameRA.OnClick {
    lateinit var binding: ActivityTimeTableBinding
    lateinit var timeTableVM: TimeTableVM
    var tableName = TableName()
    var tableTask = TableTask()
    var viewTimeTable = ViewTimeTable()
    var table = Table()
    lateinit var receiver: BroadcastReceiver
    lateinit var tableNameRA : TableNameRA
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_time_table)
        timeTableVM = ViewModelProvider(this, TimeTableVMF(
            TimeTableRepository(this)
        )
        )[TimeTableVM::class.java]
        createNotificationChannel()

        receiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                Log.e("HERE","IN RECEIVER")
                if (p1 != null) {
                    val time = p1.getStringExtra("time").toString()

                    binding.time.text = Constants.miltotime(time.toLong())
//                    val ms = String.format(Locale.US,
//                        "%02d:%02d:%02d", time / 3600,
//                        time % 3600 / 60, time % 60
//                    )
                }
            }
        }





        binding.recview.apply {
            tableNameRA = TableNameRA(ArrayList(),this@TimeTable)
            adapter = tableNameRA
            layoutManager = GridLayoutManager(this@TimeTable,1, GridLayoutManager.VERTICAL,false)
        }

        binding.newtable.setOnClickListener {
            table = Table()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.frame,table)
            transaction.addToBackStack("name")
            transaction.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("HERE","RESUME")
        lifecycleScope.launchWhenCreated {
            tableNameRA.timeTData = timeTableVM.timeTableRepository.getName()
            tableNameRA.notifyDataSetChanged()
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(IntentFilter("com.hkngtech.timer.countdown")))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun addtask() {
        supportFragmentManager.popBackStack()
        tableTask = TableTask()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,tableTask)
        transaction.addToBackStack("task")
        transaction.commit()
    }

    override fun onclick(which : Int,timeTData: TimeTData) {
        if(which == 0){
            viewTimeTable = ViewTimeTable()
            val bundle = Bundle()
            bundle.putInt("tableid",timeTData.id)
            bundle.putString("name",timeTData.name)
            viewTimeTable.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame,viewTimeTable)
            transaction.addToBackStack("view")
            transaction.commit()
        }else if(which == 1){
            Intent(this,CountDown::class.java).apply {
                putExtra("tableid",timeTData.id)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                }else
                    startService(this)
            }
        }else if(which == 2){
            lifecycleScope.launchWhenCreated {
                val res = timeTableVM.delete(timeTData.id)
                lifecycleScope.launchWhenCreated {
                    tableNameRA.timeTData = timeTableVM.timeTableRepository.getName()
                    tableNameRA.notifyDataSetChanged()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}