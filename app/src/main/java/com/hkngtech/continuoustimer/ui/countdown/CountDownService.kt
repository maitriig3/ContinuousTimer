package com.hkngtech.continuoustimer.ui.countdown

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hkngtech.continuoustimer.R
import com.hkngtech.continuoustimer.data.local.room.TimerUpdate
import com.hkngtech.continuoustimer.data.local.room.entity.Tasks
import com.hkngtech.continuoustimer.data.local.room.entity.TimeTtaskData
import com.hkngtech.continuoustimer.data.local.room.repository.RoomRepository
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.others.Constants
import com.hkngtech.continuoustimer.utils.LiveDataTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CountDownService : Service() {

    var countDownTimer : CountDownTimer? = null
    var notifcationBuilder : NotificationCompat.Builder? =null
    var notificationManager : NotificationManagerCompat? = null
    lateinit var receiver: BroadcastReceiver
    lateinit var intentAcknowledged: PendingIntent

    var tableid = 0
    var acknowledged = false

    /**
     * To show high priority notification only when each tasks are started for first time
     */
    var firstInTick = false

    @Inject lateinit var roomRepository: RoomRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            notifcationBuilder = NotificationCompat.Builder(this@CountDownService,getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_info)
                .setOnlyAlertOnce(true)

            notificationManager = NotificationManagerCompat.from(this)
            startForeground(23, notifcationBuilder!!.build())
            tableid = intent.getIntExtra("schedule_id",-1)
            countdown()

        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }
        val intent = Intent(this,receiver::class.java).apply {
            putExtra(ACKNOWLEDGED,true)
        }
        intentAcknowledged =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun countdown(){
        GlobalScope.launch(Dispatchers.IO) {
            val taskdata = roomRepository.getAllTasksService(tableid)
            withContext(Dispatchers.Main){
                if(taskdata.isNotEmpty()){
                    countdowneach(taskdata,0,taskdata.size)
                }
            }
        }
    }

    fun countdowneach(taskdata : List<Tasks>, cnt : Int, total : Int){
        val time = taskdata[cnt].time.toLong() *60 *1000
        countDownTimer = object : CountDownTimer(time,1000){
            @SuppressLint("MissingPermission")
            override fun onTick(p0: Long) {
                Log.e("COUNT",p0.toString())
//                if(firstInTick){
//                    firstInTick = false
//                    notificationBuilder()
//                    notifcationBuilder?.priority = NotificationCompat.PRIORITY_HIGH
//                }else
//                    notifcationBuilder?.priority = NotificationCompat.PRIORITY_DEFAULT
                notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                notifcationBuilder?.setContentText(taskdata[cnt].task)
                notificationManager?.notify(23,notifcationBuilder!!.build())
                LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString()))
            }

            override fun onFinish() {
//                var player = MediaPlayer()
                var ringtone : Ringtone? = null
                var ringtoneL : Ringtone? = null
                try{
//                    player = MediaPlayer.create(this@CountDown,Settings.System.DEFAULT_RINGTONE_URI)
//                    player.start()
                    if(cnt+1<total) {
                        val notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                        ringtone = RingtoneManager.getRingtone(applicationContext, notif)
                        ringtone.play()
                    }else{
                        val notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        ringtoneL = RingtoneManager.getRingtone(applicationContext, notif)
                        ringtoneL.play()
                    }
                }catch(e :Exception){

                }
                countDownTimer = object : CountDownTimer(5000,1000){
                    @SuppressLint("MissingPermission")
                    override fun onTick(p0: Long) {
                        Log.e("COUNT",p0.toString())
//                        if(firstInTick){
//                            firstInTick = false
//                            notificationBuilderWithAction()
//                            notifcationBuilder?.priority = NotificationCompat.PRIORITY_HIGH
//                        }else
//                            notifcationBuilder?.priority = NotificationCompat.PRIORITY_DEFAULT
                        if(cnt+1<total) {
                            notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notifcationBuilder?.setContentText("Next Task ${taskdata[cnt + 1].task}")
                            notificationManager?.notify(23, notifcationBuilder!!.build())
                            LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString()))
                        }else{
                            notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notifcationBuilder?.setContentText("It's Over! you did it")
                            notificationManager?.notify(23, notifcationBuilder!!.build())
                            LiveDataTimer.timerUpdate.postValue(TimerUpdate(p0.toString()))
                        }
                    }

                    override fun onFinish() {
                        try{
//                            player.stop()
                            if(cnt+1<total) {
                                ringtone?.stop()
                            }else{
                                ringtoneL?.stop()
                            }
                        }catch(e :Exception){

                        }
                        if(cnt+1<total){
                            countdowneach(taskdata,cnt+1,total)
                        }else{
                            stopSelf()
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
        return null
    }

    private fun notificationBuilder(){
        notifcationBuilder = NotificationCompat.Builder(this@CountDownService,getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_info)
            .setOnlyAlertOnce(true)
    }

    private fun notificationBuilderWithAction(){
        notifcationBuilder = NotificationCompat.Builder(this@CountDownService,getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_info)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_hand, ACKNOWLEDGE,intentAcknowledged)
    }

    companion object{
        const val ACKNOWLEDGED = "acknowledged"
        const val ACKNOWLEDGE = "Acknowledge"
    }

}