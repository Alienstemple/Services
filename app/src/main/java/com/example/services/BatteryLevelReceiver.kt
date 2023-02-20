package com.example.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log

class BatteryLevelReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        // TODO create and show notification

        val batteryPct = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)

        Log.d(TAG, "onReceive() called with: context = $context, intent = $intent")


    }

    companion object {
        const val TAG = "BattLevelLog"
    }
}