package com.example.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class TimerForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFIC_ID, createNotification(1000))
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun createNotification(currentTime: Int): Notification {
        Log.d(TAG, "createNotification() called")
        // Pending intent нужен для взаимод с activity
        val intent = Intent(this, ForegroundTimer::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

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
        const val TAG = "TimerForegrServ"
        const val NOTIFIC_ID = 1
        const val CHANNEL_ID = "Test channel"
    }
}