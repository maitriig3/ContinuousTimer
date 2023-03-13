package com.hkngtech.continuoustimer

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hkngtech.continuoustimer.data.local.entity.TimeTtaskData
import com.hkngtech.continuoustimer.model.repository.TimeTableRepository
import com.hkngtech.continuoustimer.others.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountDown : Service() {

    var countDownTimer : CountDownTimer? = null
    var notifcationBuilder : NotificationCompat.Builder? =null
    var notificationManager : NotificationManagerCompat? = null

    var tableid = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("START","START $intent")
        if (intent != null) {
            notifcationBuilder = NotificationCompat.Builder(this@CountDown,getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_info)
                .setOnlyAlertOnce(true)
            notificationManager = NotificationManagerCompat.from(this)
            startForeground(23, notifcationBuilder!!.build())
            tableid = intent.getIntExtra("tableid",0)
            countdown()

        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()

    }


    fun countdown(){
        val timeTableRepository = TimeTableRepository(this)


        Log.e("TABLEID",tableid.toString())
        GlobalScope.launch(Dispatchers.IO) {
            val taskdata = timeTableRepository.getTask(tableid)
            Log.e("TASKDATA",taskdata.toString())
            withContext(Dispatchers.Main){
                if(taskdata.size>0){

                    countdowneach(taskdata,0,taskdata.size)
                }
            }
        }
    }

    fun countdowneach(taskdata : List<TimeTtaskData>, cnt : Int, total : Int){
        val time = taskdata[cnt].time.toLong() *60 *1000
        val broadcaster = LocalBroadcastManager.getInstance(this@CountDown)
        val intent = Intent("com.hkngtech.timer.countdown")
        countDownTimer = object : CountDownTimer(time,1000){
            override fun onTick(p0: Long) {
                Log.e("COUNT",p0.toString())
                notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                notifcationBuilder?.setContentText(taskdata[cnt].task)
                notificationManager?.notify(23,notifcationBuilder!!.build())
                intent.putExtra("time",p0.toString())
                broadcaster.sendBroadcast(intent)
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
                    override fun onTick(p0: Long) {
                        Log.e("COUNT",p0.toString())
                        if(cnt+1<total) {
                            notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notifcationBuilder?.setContentText("Next Task ${taskdata[cnt + 1].task}")
                            notificationManager?.notify(23, notifcationBuilder!!.build())
                            intent.putExtra("time", p0.toString())
                            broadcaster.sendBroadcast(intent)
                        }else{
                            notifcationBuilder?.setContentTitle(Constants.miltotime(p0))
                            notifcationBuilder?.setContentText("It's Over! you did it")
                            notificationManager?.notify(23, notifcationBuilder!!.build())
                            intent.putExtra("time", p0.toString())
                            broadcaster.sendBroadcast(intent)
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
                intent.putExtra("time","5000")
                broadcaster.sendBroadcast(intent)
                countDownTimer?.start()
            }

        }
        intent.putExtra("time",time.toString())
        broadcaster.sendBroadcast(intent)
        countDownTimer?.start()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }



}