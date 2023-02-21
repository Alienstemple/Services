package com.example.services

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class BatteryLevelService : Service() {

    private val binder = BatteryLevelServiceBinder()

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(TimerForegroundService.NOTIFIC_ID, createNotification(1000))

        // start timer if not ACTION_STOP

        return START_NOT_STICKY
    }

    private fun createNotification(currentTime: Int): Notification {
        Log.d(TAG, "createNotification() called")
        // Pending intent нужен для взаимод с activity
        val intent = Intent(this, ForegroundTimer::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE) // Android 12

        // TODO intent for close

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notific_text_title))
            .setContentText(getString(R.string.notific_text_descr) + currentTime)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
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

    companion object {
        const val TAG = "BatterLevServLog"
        const val NOTIFIC_ID = 101
        const val CHANNEL_ID = "Battery channel"
    }
}