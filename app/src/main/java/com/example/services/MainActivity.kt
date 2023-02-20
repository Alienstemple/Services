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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.backgroundServiceBtn.setOnClickListener {
            intent = Intent(this, BackgroundTimer::class.java)
            startActivity(intent)
        }

        mainBinding.foregroundServiceBtn.setOnClickListener {
            intent = Intent(this, ForegroundTimer::class.java)
            startActivity(intent)
        }

        mainBinding.batteryManagerNotificBtn.setOnClickListener {
            intent = Intent(this, BatteryManagerNotificatoinActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "MainActLog"
    }
}