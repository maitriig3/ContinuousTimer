package com.hkngtech.continuoustimer.ui.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.data.local.room.TimerUpdate
import com.hkngtech.continuoustimer.databinding.FragmentScheduleBinding
import com.hkngtech.continuoustimer.others.Constants
import com.hkngtech.continuoustimer.ui.base.BaseFragment
import com.hkngtech.continuoustimer.ui.countdown.CountDownService
import com.hkngtech.continuoustimer.ui.history.HistoryFragmentDirections
import com.hkngtech.continuoustimer.ui.schedule.adapter.ScheduleAdapter
import com.hkngtech.continuoustimer.utils.LiveDataTimer
import com.hkngtech.continuoustimer.utils.logE
import com.hkngtech.continuoustimer.utils.navigate
import com.hkngtech.continuoustimer.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {

    private val scheduleViewModel by viewModels<ScheduleViewModel>()
    lateinit var observer: Observer<TimerUpdate>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()

        observer = Observer<TimerUpdate>{
            "LIVE DATA ${it.time}".logE()
            if (it != null) {
                val time = it.time

                binding.time.text = Constants.miltotime(time.toLong())
//                    val ms = String.format(Locale.US,
//                        "%02d:%02d:%02d", time / 3600,
//                        time % 3600 / 60, time % 60
//                    )
            }
        }

        binding.recViewSchedule.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.btnNewSchedule.setOnClickListener {
            navigate(ScheduleFragmentDirections.actionScheduleFragmentToTasksFragment(-1))
        }

        binding.imgHistory.setOnClickListener {
            navigate(ScheduleFragmentDirections.actionScheduleFragmentToHistoryFragment())
        }

        lifecycleScope.launchWhenCreated {
            scheduleViewModel.getAll().collectLatest {
                binding.recViewSchedule.adapter = ScheduleAdapter(it) { which, schedule ->
                    if (which == 0) {
                        navigate(
                            ScheduleFragmentDirections.actionScheduleFragmentToTasksFragment(
                                schedule.scheduleId
                            )
                        )
                    } else if (which == 1) {
                        if(scheduleViewModel.serviceIsNotRunning){
                            if(isIgnoringBatteryOptimization()){
                                Intent(requireContext(), CountDownService::class.java).apply {
                                    putExtra("schedule_id", schedule.scheduleId)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        requireActivity().startForegroundService(this)
                                    } else
                                        requireActivity().startService(this)
                                    scheduleViewModel.serviceIsNotRunning = false
                                }
                            }
                        }else{
                            "Timer is running".toast(requireContext())
                        }
                    } else if (which == 2) {
                        lifecycleScope.launchWhenCreated {
                            scheduleViewModel.delete(schedule.scheduleId)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LiveDataTimer.timerUpdate.observe(requireActivity(),observer)
    }

    override fun onPause() {
        super.onPause()
        LiveDataTimer.timerUpdate.removeObserver(observer)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isIgnoringBatteryOptimization(): Boolean {
        val intent = Intent()
        val packageName: String = requireActivity().packageName
        val pm = requireContext().getSystemService(POWER_SERVICE) as PowerManager
        return if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//            intent.data = Uri.parse("package:$packageName")
            requireActivity().startActivity(intent)
            false
        } else
            true
    }
}