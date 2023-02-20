package com.example.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.services.databinding.ActivityForegroundTimerBinding
import com.example.services.databinding.ActivityMainBinding

class ForegroundTimer : AppCompatActivity() {

    private lateinit var foregrBinding: ActivityForegroundTimerBinding
    private lateinit var timerService: TimerBackgroundService

    var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")

        foregrBinding = ActivityForegroundTimerBinding.inflate(layoutInflater)
        setContentView(foregrBinding.root)

        foregrBinding.startForegroundBtn.setOnClickListener {
            startTimer()
        }
        foregrBinding.stopForegroundBtn.setOnClickListener {
            stopTimer()
        }
    }

    private inner class TimerServiceConnection: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerBackgroundService.LocalTimeServiceBinder
            timerService = binder.getTimerService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            Log.d(TAG, "onServiceDisconnected() called with: name = $name")
        }
    }

    private fun startTimer() {
        intent = Intent(this, TimerForegroundService::class.java)
        startService(intent)
    }

    private fun stopTimer() {
        intent = Intent(this, TimerForegroundService::class.java)
        stopService(intent)
    }

    private fun bindService() {
        intent = Intent(this, TimerForegroundService::class.java)
//        val timerServiceConnection = ServiceConnection
//        bindService(intent, timerServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        const val TAG = "MainActLog"
    }
}