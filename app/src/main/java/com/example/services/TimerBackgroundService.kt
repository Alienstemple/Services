package com.example.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.TimePicker.OnTimeChangedListener

class TimerBackgroundService : Service() {

    private val myBinder = LocalTimeServiceBinder()
    private lateinit var countdownTimer: CountDownTimer

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind() called with: intent = $intent")
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG,
            "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId")

        if(intent.action == ACTION_CLOSE) {
             stopSelf()
        } else {
             startCountDownTimer(TIME_COUNTDOWN, TIMER_PERIOD)
        }

        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.d(TAG, "onUnbind() called with: intent = $intent")
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind() called with: intent = $intent")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
        stopCountdownTimer()
    }

    inner class LocalTimeServiceBinder : Binder() {
        fun getTimerService() = this@TimerBackgroundService
    }

    private fun startCountDownTimer(time: Long, period: Long) {
        countdownTimer = object: CountDownTimer(time, period) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick() called with: millisUntilFinished = $millisUntilFinished")
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish() called")
                stopCountdownTimer()
            }
        }

        countdownTimer.start()
    }

    private fun stopCountdownTimer() {
        Log.d(TAG, "stopCountdownTimer() called")
        countdownTimer.cancel()
        // remove link
    }

    companion object {
        const val TAG = "TimerServTag"
        const val ACTION_CLOSE = "TIMER_ACTION_CLOSE"
        const val TIME_COUNTDOWN = 1000 * 500L
        const val TIMER_PERIOD = 1000L
    }
}