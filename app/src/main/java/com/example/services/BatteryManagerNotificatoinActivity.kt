package com.example.services

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                checkNotificationPermission()
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

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            // Granted
            Log.d(TAG, "Notification permission is already granted")
            startService()
        } else {
            // Not granted
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        Log.d(TAG, "In requestNotificPerm")
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIF_PERM_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "In onRequestPermissionResult")

        if (requestCode == NOTIF_PERM_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                Log.d(TAG, "Notific permitted")
                startService()
            } else {
                Log.d(TAG, "Notific permission is still DENIED")
                Toast.makeText(this, "Notific permission is still DENIED", Toast.LENGTH_SHORT)
                    .show()
            }
            return
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
        val intent = Intent(this, BatteryLevelService::class.java)
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
        const val NOTIF_PERM_REQUEST_CODE = 101
    }
}