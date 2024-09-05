package com.example.abc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.demo_full.Activity_Foulder
import com.example.demo_full.databinding.ActivitySplashscreenBinding
import com.example.gallery.MainActivity11

class splashscreen : AppCompatActivity() {
    var progressStatus = 0
    private lateinit var handler: Handler
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivitySplashscreenBinding
    private val SPLASH_TIME = 5000 // 5 seconds for splash screen

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestStoragePermissions()

        // Initialize Handler with the main thread's Looper
        handler = Handler(Looper.getMainLooper())

        // Start a thread for the splash screen progress
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post {
                    binding.progressBar2.progress = progressStatus
                    binding.tvprogress.text = "$progressStatus%"
                }
                try {
                    // Sleep for the appropriate time based on SPLASH_TIME and progress update
                    Thread.sleep((SPLASH_TIME / 100).toLong())
                } catch (e: InterruptedException) {
                    Log.e("Exception", "onCreate: $e")
                }
            }

            // After progress is complete, navigate to the main activity
            val intent = Intent(this@splashscreen, MainActivity11::class.java)
            startActivity(intent)
            finish()
        }.start()
    }

    // QR Code handling functions (unchanged)
    fun onQRCodeScanned(result: String?) {
        val type = determineQRCodeType(result!!)
        handleQRCodeResult(result, type!!)
    }

    private fun determineQRCodeType(result: String): String? {
        return when {
            result.startsWith("http://") || result.startsWith("https://") -> "URL"
            result.startsWith("mailto:") -> "Email"
            result.startsWith("tel:") -> "Phone Number"
            result.startsWith("sms:") -> "SMS"
            result.startsWith("geo:") -> "Location"
            else -> "Text"
        }
    }

    private fun handleQRCodeResult(result: String, type: String) {
        when (type) {
            "URL" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result)))
            "Email" -> startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(result)))
            "Phone Number" -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(result)))
            "SMS" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result)))
            "Location" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result)))
            else -> Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }
    }

    // Permission request functions (unchanged)
    private fun requestStoragePermissions() {
        val permissions: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission granted for " + permissions[i])
                    } else {
                        Log.d("Permissions", "Permission denied for " + permissions[i])
                    }
                }
            }
        }
    }
}
