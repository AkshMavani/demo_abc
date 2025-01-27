package com.foregorund.adapter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.demo_full.R
import com.foregorund.Window

class MyForegroundService : Service() {
    var reciver:ScreenReceiver?=null
    private var isWindowOpen = false

    override fun onCreate() {
        reciver = ScreenReceiver()
        super.onCreate()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(reciver, filter)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isWindowClosed = intent.getBooleanExtra("window_closed", false)
        val isScreenOn = intent.getBooleanExtra("a", false)

        if (isWindowClosed) {
            Log.e("Service", "Window closed")
            isWindowOpen = false
        }

        if (isScreenOn && !isWindowOpen) {
            Log.e("Service", "Opening Window on screen ON")
            val window = Window(context = this)
            window.open()
            isWindowOpen = true
        }

        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY // Ensure the service restarts if terminated
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity22::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
        builder.setContentTitle(getString(R.string.app_name))
        builder.setContentText(getString(R.string.app_name))
        builder.setContentIntent(pendingIntent)
        return builder.build()
    }

    companion object {
        private const val CHANNEL_ID = "my_channel_id"
        private const val NOTIFICATION_ID = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(reciver)
    }
}

