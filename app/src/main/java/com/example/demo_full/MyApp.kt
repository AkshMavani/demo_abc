package com.example.demo_full

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast

class MyApp : Application() {

    private lateinit var networkChangeReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()

        networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                context?.let {
                    val connectivityManager = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val networkInfo = connectivityManager.activeNetworkInfo
                    if (networkInfo != null && networkInfo.isConnected) {
                        Toast.makeText(it, "Internet Connected", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(it, "Internet Disconnected", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(networkChangeReceiver)
    }
}