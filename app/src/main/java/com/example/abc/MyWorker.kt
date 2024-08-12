package com.example.abc

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // Start the background service
        val serviceIntent = Intent(applicationContext, BackgroundService::class.java)
        applicationContext.startService(serviceIntent)
        // Log the action
        Log.d("MyWorker", "BackgroundService started by MyWorker")
        // Return success if the task completes successfully
        return Result.success()
    }
}