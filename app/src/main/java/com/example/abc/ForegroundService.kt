package com.example.abc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.demo_full.R
class BackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroundService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BackgroundService", "Service started")
        // Perform your background tasks here
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackgroundService", "Service destroyed")
        // Schedule the service to restart
        scheduleRestart()
    }

    private fun scheduleRestart() {
        val restartIntent = Intent(this, BackgroundService::class.java)
        startService(restartIntent)
    }
}