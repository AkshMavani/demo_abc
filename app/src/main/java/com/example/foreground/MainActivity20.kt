package com.example.foreground

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.demo_full.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity20 : AppCompatActivity() {
    private var exampleService: ExampleLocationForegroundService? = null

    private var serviceBoundState = false
    private var displayableLocation: String? = null

    private lateinit var startStopButton: Button
    private lateinit var locationTextView: TextView

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d("TAG", "onServiceConnected")


            val binder = service as ExampleLocationForegroundService.LocalBinder
            exampleService = binder.getService()
            serviceBoundState = true

            onServiceConnected()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d("TAG", "onServiceDisconnected")
            serviceBoundState = false
            exampleService = null
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main20)
        startStopButton = findViewById(R.id.start_stop_button)
        locationTextView = findViewById(R.id.location_text_view)

        startStopButton.setOnClickListener {
            onStartOrStopForegroundServiceClick()
        }

        checkAndRequestNotificationPermission()
        tryToBindToServiceIfRunning()

    }
    override fun onDestroy() {
        super.onDestroy()
        if (serviceBoundState) {
            unbindService(connection)
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun onStartOrStopForegroundServiceClick() {
        if (exampleService == null) {
            // Service is not running, start it after location permission check
            checkLocationPermissionAndStartService()
        } else {
            // Service is running, stop it
            exampleService?.stopForegroundService()
        }
    }

    private fun checkLocationPermissionAndStartService() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startForegroundService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startForegroundService() {
        val intent = Intent(this, ExampleLocationForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        tryToBindToServiceIfRunning()
    }

    private fun tryToBindToServiceIfRunning() {
        val intent = Intent(this, ExampleLocationForegroundService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun onServiceConnected() {
        lifecycleScope.launch {
            exampleService?.locationFlow?.map {
                it?.let { location ->
                    "${location.latitude}, ${location.longitude}"
                }
            }?.collectLatest {
                displayableLocation = it
                locationTextView.text = displayableLocation
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1002
    }
}