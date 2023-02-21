package com.example.services

interface BatteryChangedListener {
    fun onBatteryLevelChanged(batteryPct: Int)
}