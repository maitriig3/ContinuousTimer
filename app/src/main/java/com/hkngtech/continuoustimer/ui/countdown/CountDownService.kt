package com.hkngtech.continuoustimer.ui.countdown

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.data.local.room.TimerUpdate
import com.hkngtech.continuoustimer.data.local.room.entity.History
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import com.hkngtech.continuoustimer.others.Constants
import com.hkngtech.continuoustimer.utils.LiveDataTimer
import com.hkngtech.continuoustimer.utils.logE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CountDownService : LifecycleService() {

    var countDownTimer: CountDownTimer? = null
    var notificationBuilder: NotificationCompat.Builder? = null
    var notificationManager: NotificationManagerCompat? = null
    lateinit var receiver: BroadcastReceiver
    lateinit var intentAcknowledged: PendingIntent

    var scheduleId = 0

    /**
     * To show high priority notification only when each tasks are started for first time
     */
    var firstInTick = false

    @Inject
    lateinit var roomRepository: RoomRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            notificationBuilder =
                NotificationCompat.Builder(this@CountDownService, getString(R.string.channel_id))
                    .setSmallIcon(R.drawable.ic_info)
                    .setOnlyAlertOnce(true)

            notificationManager = NotificationManagerCompat.from(this)
            startForeground(23, notificationBuilder!!.build())
            scheduleId = intent.getIntExtra("schedule_id", -1)
            countDown()

        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                notificationBuilder()
            }
        }
        val intent = Intent(this, AcknowledgeBroadcast::class.java).apply {
            putExtra(ACKNOWLEDGED, true)
        }
        intentAcknowledged =
            PendingIntent.getBroadcast(this, 23, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun countDown() {
        lifecycleScope.launch(Dispatchers.IO) {
            val taskData = roomRepository.getAllTasksService(scheduleId)
            roomRepository.insertHistory(scheduleId)
            val history = roomRepository.getRecentHistory()
            withContext(Dispatchers.Main) {
                if (taskData.isNotEmpty()) {
                    countDownEach(taskData, 0, taskData.size, history)
                }
            }
        }
    }

    fun countDownEach(tasks: List<Tasks>, cnt: Int, total: Int, history: History) {
        val time = when (tasks[cnt].timeUnit) {
            "S" -> tasks[cnt].time.toLong() * 1000
            "M" -> tasks[cnt].time.toLong() * 60 * 1000
            "H" -> tasks[cnt].time.toLong() * 60 * 60 * 1000
            else -> tasks[cnt].time.toLong() * 60 * 1000
        }
        countDownTimer = object : CountDownTimer(time, 1000) {
            @SuppressLint("MissingPermission")
            override fun onTick(p0: Long) {
                Log.e("COUNT", p0.toString())
                if (firstInTick) {
                    firstInTick = false
                    if(cnt == 0)
                        notificationBuilder()
                    else
                        notificationBuilderWithAction()
                    notificationBuilder?.priority = NotificationCompat.PRIORITY_HIGH
                } else
                    notificationBuilder?.priority = NotificationCompat.PRIORITY_DEFAULT
                notificationBuilder?.setContentTitle(Constants.miltotime(p0))
                notificationBuilder?.setContentText(tasks[cnt].task)
                notificationManager?.notify(23, notificationBuilder!!.build())
                LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString()))
            }

            override fun onFinish() {
//                var player = MediaPlayer()
                lifecycleScope.launch(Dispatchers.IO) {
                    roomRepository.insertHistoryDetails(
                        history.historyId,
                        scheduleId,
                        tasks[cnt].tasksId,
                        false
                    )
                }
                var ringtone: Ringtone? = null
                var ringtoneL: Ringtone? = null
                try {
//                    player = MediaPlayer.create(this@CountDown,Settings.System.DEFAULT_RINGTONE_URI)
//                    player.start()
                    if (cnt + 1 < total) {
                        val notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                        ringtone = RingtoneManager.getRingtone(applicationContext, notif)
                        ringtone.play()
                    } else {
                        val notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        ringtoneL = RingtoneManager.getRingtone(applicationContext, notif)
                        ringtoneL.play()
                    }
                } catch (_: Exception) {

                }
                countDownTimer = object : CountDownTimer(5000, 1000) {
                    @SuppressLint("MissingPermission")
                    override fun onTick(p0: Long) {
                        Log.e("COUNT", p0.toString())
                        if (firstInTick) {
                            firstInTick = false
                            notificationBuilderWithAction()
                            notificationBuilder?.priority = NotificationCompat.PRIORITY_HIGH
                        } else
                            notificationBuilder?.priority = NotificationCompat.PRIORITY_DEFAULT
                        if (cnt + 1 < total) {
                            notificationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notificationBuilder?.setContentText("Next Task ${tasks[cnt + 1].task}")
                            notificationManager?.notify(23, notificationBuilder!!.build())
                            LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString()))
                        } else {
                            notificationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notificationBuilder?.setContentText("It's Over! you did it")
                            notificationManager?.notify(23, notificationBuilder!!.build())
                            LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString(), true))
                        }
                    }

                    override fun onFinish() {
                        try {
//                            player.stop()
                            if (cnt + 1 < total) {
                                ringtone?.stop()
                            } else {
                                ringtoneL?.stop()
                            }
                        } catch (_: Exception) {

                        }
                        if (cnt + 1 < total) {
                            countDownEach(tasks, cnt + 1, total, history)
                        } else {
                            lifecycleScope.launch(Dispatchers.IO) {
                                roomRepository.updateHistory(history)
                                withContext(Dispatchers.Main){
                                    stopSelf()
                                }
                            }
                        }
                    }

                }
                LiveDataTimer.timerUpdate.postValue(TimerUpdate("5000"))
                firstInTick = true
                countDownTimer?.start()
            }

        }
        LiveDataTimer.timerUpdate.postValue(TimerUpdate(time.toString()))
        firstInTick = true
        countDownTimer?.start()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun notificationBuilder() {
        notificationBuilder =
            NotificationCompat.Builder(this@CountDownService, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_info)
                .setOnlyAlertOnce(true)
    }

    private fun notificationBuilderWithAction() {
        notificationBuilder =
            NotificationCompat.Builder(this@CountDownService, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_info)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_hand, ACKNOWLEDGE, intentAcknowledged)
    }

    companion object {
        const val ACKNOWLEDGED = "acknowledged"
        const val ACKNOWLEDGE = "Acknowledge"
    }

}

@AndroidEntryPoint
class AcknowledgeBroadcast : BroadcastReceiver() {

    @Inject
    lateinit var roomRepository: RoomRepository
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        "BROADCAST RECEIVER".logE()
        GlobalScope.launch(Dispatchers.IO) {
            roomRepository.updateHistoryDetails()
        }
    }

}