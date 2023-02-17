package com.example.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var timerService: TimerService

    var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.startBtn.setOnClickListener {
            startTimer()
        }
        mainBinding.stopBtn.setOnClickListener {
            stopTimer()
        }
    }

    private inner class TimerServiceConnection: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.LocalTimeServiceBinder
            timerService = binder.getTimerService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            Log.d(TAG, "onServiceDisconnected() called with: name = $name")
        }
    }

    private fun startTimer() {
        intent = Intent(this, TimerService::class.java)
        startService(intent)
    }

    private fun stopTimer() {
        intent = Intent(this, TimerService::class.java)
        stopService(intent)
    }

    private fun bindService() {
        intent = Intent(this, TimerService::class.java)
//        val timerServiceConnection = ServiceConnection
//        bindService(intent, timerServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        const val TAG = "MainActLog"
    }
}