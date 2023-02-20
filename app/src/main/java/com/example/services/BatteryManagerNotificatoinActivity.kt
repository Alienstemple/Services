package com.example.services

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BatteryManagerNotificatoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_manager_notificatoin)

        registerReceiver(BatteryLevelReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
}