package com.foregorund.adapter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.foregorund.MyService

class ScreenReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            Log.e("ScreenReceiver", "Screen ON detected")
            val serviceIntent = Intent(context, MyForegroundService::class.java)
            serviceIntent.putExtra("a", true)
            context.startService(serviceIntent)
        } else if (intent.action == Intent.ACTION_SCREEN_OFF) {
            Log.e("ScreenReceiver", "Screen OFF detected")
        }
    }
}