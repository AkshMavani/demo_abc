package com.foregorund

import android.content.ContentValues.TAG

import com.example.abc.MyWorker
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.demo_full.R
import java.util.concurrent.TimeUnit


class MainActivity21 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main21)
        startServiceViaWorker()
        startService()
    }
    fun onStartServiceClick(v: View?) {
        startService()
    }

    fun onStopServiceClick(v: View?) {
        stopService()
    }

/*
    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        stopService()
        super.onDestroy()
    }
*/

    fun startService() {
        Log.d(TAG, "startService called")
        if (!MyService.isServiceRunning) {
            val serviceIntent = Intent(this, MyService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }

    fun stopService() {
        Log.d(TAG, "stopService called")
        if (MyService.isServiceRunning) {
            val serviceIntent = Intent(this, MyService::class.java)
            stopService(serviceIntent)
        }
    }

    fun startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called")
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker"
        val workManager = WorkManager.getInstance(this)

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        val request: PeriodicWorkRequest =   PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            16,
            TimeUnit.MINUTES
        )
            .build()

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}