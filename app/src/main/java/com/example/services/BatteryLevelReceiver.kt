package com.example.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log

class BatteryLevelReceiver(private val batteryChangedListener: BatteryChangedListener): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val batteryPct = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        Log.d(TAG, "onReceive() Battery level = $batteryPct")

        batteryChangedListener.onBatteryLevelChanged(batteryPct)
    }

    companion object {
        const val TAG = "BattLevelLog"
    }
}