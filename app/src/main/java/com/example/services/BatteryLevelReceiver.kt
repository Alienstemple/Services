package com.example.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BatteryLevelReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO create and show notification
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

    }
}