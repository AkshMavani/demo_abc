package com.foregorund

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.abc.MyWorker
import com.example.demo_full.R
import java.util.concurrent.TimeUnit


class MainActivity21 : AppCompatActivity() {
    private val REQUEST_CODE_LOCATION = 1001
    private val REQUEST_CODE_NOTIFICATION = 1002
    private val REQUEST_NOTIFICATION_PERMISSION = 101
    private val REQUEST_PERMISSIONS = 1001
    private val REQUEST_OVERLAY_PERMISSION = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main21)
        requestOverlayPermission()
        checkAndRequestPermissions()
        startServiceViaWorker()
        startService()
      /*  val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        startActivity(myIntent)*/
     /*   startServiceViaWorker()
        startService()*/
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

    private fun checkAndRequestPermissions() {
        // Permissions to check
        val permissionsToRequest = mutableListOf<String>()

        // Check for POST_NOTIFICATIONS permission (Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Request permissions if any are not granted
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS
            )
        } else {
            Log.e(TAG, "checkAndRequestPermissions: ${checkOverlayPermission()}", )
         /*   val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
             startActivity(myIntent)*/

               startServiceViaWorker()
               startService()
            Log.d(TAG, "All required permissions are already granted.")
        }
    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "$permission granted.")
                    startServiceViaWorker()
                    startService()
                    // Handle permission granted actions if necessary
                } else {
                    Log.e(TAG, "$permission denied.")
                    // Show rationale or guide user to settings if permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        showPermissionRationale(permission)
                    } else {
                        Log.e(TAG, "User denied $permission permanently. Guide them to app settings.")
                    }
                }
            }
        }
    }*/
    private fun showPermissionRationale(permission: String) {
        val message = when (permission) {
            Manifest.permission.POST_NOTIFICATIONS -> "This app needs notification permission to send alerts and updates."
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> "Location permission is required to access location-based features."
            else -> "This app needs additional permissions to function properly."
        }

        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage(message)
            .setPositiveButton("Grant") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    REQUEST_PERMISSIONS
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permissions granted.")
            } else {
                Log.e(TAG, "Location permissions denied.")
            }
        }
        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted.")
            } else {
                Log.e(TAG, "Notification permission denied.")
            }
        }
    }*/
   private fun checkOverlayPermission(): Boolean {
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           Settings.canDrawOverlays(this) // Returns true if permission is granted
       } else {
           true // Overlay permission is not required for Android versions below Marshmallow (API 23)
       }
   }
    private fun requestOverlayPermission() {
        if (!checkOverlayPermission()) {
            // Permission not granted, request it
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        } else {
            Log.d("OverlayPermission", "Overlay permission is already granted.")
            // Proceed with functionality that requires overlay permission
        }
    }
}