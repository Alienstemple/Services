package com.example.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.services.databinding.ActivityBatteryManagerNotificatoinBinding

class BatteryManagerNotificatoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBatteryManagerNotificatoinBinding
    private lateinit var batteryLevelService: BatteryLevelService
    private var bound: Boolean = false
    private val batteryLevelServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "Service connected")
            batteryLevelService =
                (service as BatteryLevelService.BatteryLevelServiceBinder).getBatteryLevelService()
            startListeningTimer()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "Service disconnected")
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryManagerNotificatoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            startBatteryServiceBtn.setOnClickListener {
                startService()
            }

            stopBatteryServiceBtn.setOnClickListener {
                stopService()
            }

            bindBatteryServiceBtn.setOnClickListener {
                bindBatteryService()
            }

            unbindBatteryServiceBtn.setOnClickListener {
                unbindBatteryService()
            }

            startTimerBtn.setOnClickListener {
                startTimer()
            }
        }

    }

    private fun startService() {
        val intent = Intent(this, BatteryLevelService::class.java)
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, BatteryLevelService::class.java)
        stopService(intent)
    }

    private fun bindBatteryService() {
        val intent = Intent(this, BatteryLevelService.BatteryLevelServiceBinder::class.java)
        bindService(intent, batteryLevelServiceConnection, BIND_AUTO_CREATE)
        bound = true
    }

    private fun unbindBatteryService() {
        unbindService(batteryLevelServiceConnection)
        stopListeningTimer()
        bound = false
    }

    private fun startListeningTimer() {
//        batteryLevelService.setOnTimeChangedListener // TODO set text view
    }

    private fun stopListeningTimer() {
//        batteryLevelService.setOnTimeChangedListener(null)
    }

    private fun startTimer() {
        batteryLevelService.let {
//            it.startTimer()
        }
    }

    companion object {
        const val TAG = "BatterActivLog"
    }
}