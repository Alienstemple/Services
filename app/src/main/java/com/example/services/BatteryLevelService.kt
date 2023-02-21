package com.example.services

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.provider.Telephony
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class BatteryLevelService : Service() {

    private val binder = BatteryLevelServiceBinder()
    private lateinit var receiver: BatteryLevelReceiver
    private lateinit var countdownTimer: CountDownTimer
    private var batteryPct: Int = 100
    private var currentTimer: Int = 1000

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    inner class BatteryLevelServiceBinder : Binder() {
        fun getBatteryLevelService() = this@BatteryLevelService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind() called with: intent = $intent")
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIFIC_ID, createNotification(100, 1000))

        if(intent.action == TimerBackgroundService.ACTION_CLOSE) {
            Log.d(TAG, "Action close, before stop self")
            stopSelf()
        } else {
            Log.d(TAG, "Before starting timer")
            startCountDownTimer(TimerBackgroundService.TIME_COUNTDOWN,
                TimerBackgroundService.TIMER_PERIOD)
        }

        // Register receiver
        Log.d(TAG, "Before registering receiver")
        receiver = BatteryLevelReceiver(object : BatteryChangedListener {
            override fun onBatteryLevelChanged(batteryPct: Int) {
                Log.d(TAG, "UpdateNotification called. Battery = $batteryPct")
                this@BatteryLevelService.batteryPct = batteryPct
                updateNotification()
            }
        })
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        return START_NOT_STICKY
    }

    private fun createNotification(batterLevel: Int, currentTimer: Int): Notification {
        Log.d(TAG, "createNotification() called")
        // Pending intent нужен для взаимод с activity
        val intent = Intent(this, ForegroundTimer::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE) // Android 12

        // PendingIntent для остановки таймера
        val intentClose = Intent(ACTION_CLOSE)
        val pendingIntentClose = PendingIntent.getActivity(this, 0, intentClose, PendingIntent.FLAG_IMMUTABLE) // Android 12

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notific_text_title))
            .setContentText(" Battery level = $batterLevel% " +
                    " Current timer state = $currentTimer " +
                    " Time now = $currentDate ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(0, "Stop timer", pendingIntentClose)
            .build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.notific_channel_name)
            val description: String = getString(R.string.notific_channel_description)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationChannel.description = description
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCountdownTimer()
        unregisterReceiver(receiver)
    }

    private fun startCountDownTimer(time: Long, period: Long) {
        countdownTimer = object: CountDownTimer(time, period) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick() called with: millisUntilFinished = $millisUntilFinished")
                currentTimer = millisUntilFinished.toInt()
                updateNotification()
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish() called")
                stopCountdownTimer()
            }
        }

        countdownTimer.start()
    }

    private fun updateNotification() {
        val notificationManager = NotificationManagerCompat.from(this@BatteryLevelService)
        notificationManager.notify(NOTIFIC_ID, createNotification(batteryPct, currentTimer))
    }

    private fun stopCountdownTimer() {
        Log.d(TAG, "stopCountdownTimer() called")
        countdownTimer.cancel()
    }

    companion object {
        const val TAG = "BatterLevServLog"
        const val NOTIFIC_ID = 101
        const val CHANNEL_ID = "Battery channel"
        const val ACTION_CLOSE = "TIMER_ACTION_CLOSE"
        const val TIME_COUNTDOWN = 1000 * 500L
        const val TIMER_PERIOD = 1000L
    }
}