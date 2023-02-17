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
        foregrBinding = ActivityForegroundTimerBinding.inflate(layoutInflater)
        setContentView(foregrBinding.root)

        intent = Intent(this, BackgroundTimer::class.java)
        startActivity(intent)

//        foregrBinding..setOnClickListener {
//            startTimer()
//        }
//        foregrBinding..setOnClickListener {
//            stopTimer()
//        }
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
        intent = Intent(this, TimerBackgroundService::class.java)
        startService(intent)
    }

    private fun stopTimer() {
        intent = Intent(this, TimerBackgroundService::class.java)
        stopService(intent)
    }

    private fun bindService() {
        intent = Intent(this, TimerBackgroundService::class.java)
//        val timerServiceConnection = ServiceConnection
//        bindService(intent, timerServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        const val TAG = "MainActLog"
    }
}