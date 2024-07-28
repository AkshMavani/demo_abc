package com.example.demo_full

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class NetworkCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            showToast("Internet Connected")
        } else {
            showToast("Internet Disconnected")
        }
        return Result.success()
    }

    private fun showToast(message: String) {
        // You can use a Handler to show a toast message on the main thread
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}