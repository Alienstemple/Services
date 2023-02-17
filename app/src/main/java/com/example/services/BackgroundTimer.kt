package com.example.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.services.databinding.ActivityBackgroundTimerBinding

class BackgroundTimer : AppCompatActivity() {

    private lateinit var backgBinding: ActivityBackgroundTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgBinding = ActivityBackgroundTimerBinding.inflate(layoutInflater)
        setContentView(backgBinding.root)

        Log.d(TAG, "Background timer onCreate() called with: savedInstanceState = $savedInstanceState")

        backgBinding.startBackgroundBtn.setOnClickListener {
            Log.d(TAG, "Start backgr btn pressed")
            startTimer()
        }
        backgBinding.stopBackgroundBtn.setOnClickListener {
            Log.d(TAG, "Stop backgr btn pressed")
            stopTimer()
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

    companion object {
        const val TAG = "BackgrTimerLog"
    }
}