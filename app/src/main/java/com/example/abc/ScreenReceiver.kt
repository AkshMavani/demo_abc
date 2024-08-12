package com.example.abc

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            when (intent.action) {
                Intent.ACTION_SCREEN_ON -> {
                    Log.d("ScreenReceiver", "Screen ON")
                    context.startService(Intent(context, BackgroundService::class.java))
                }
                Intent.ACTION_SCREEN_OFF -> {
                    Log.d("ScreenReceiver", "Screen OFF")
                    context.stopService(Intent(context, BackgroundService::class.java))
                }
            }
        }
    }
}