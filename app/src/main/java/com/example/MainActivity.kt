package com.example

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.abc.MyWorker
import com.example.abc.ScreenReceiver
import com.example.demo_full.R
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var screenReceiver: ScreenReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)
        val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(15, TimeUnit.MINUTES)
            .build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

}