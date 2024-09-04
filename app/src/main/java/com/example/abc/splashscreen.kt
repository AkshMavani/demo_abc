package com.example.abc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.Activity_Foulder
import com.example.demo_full.databinding.ActivitySplashscreenBinding


class splashscreen : AppCompatActivity() {
    var progressStatus = 0
    var handler = Handler()
    private lateinit var binding:ActivitySplashscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post {
                    binding.progressBar2.progress = progressStatus
                    binding.tvprogress.text = "$progressStatus%"

                }
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    Log.e("Exception is", "onCreate:$e " )
                }
            }
            if (progressStatus == 100) {
                val i = Intent(this, Activity_Foulder::class.java)
                startActivity(i)
                finish()
            }

        }.start()

    }
    fun onQRCodeScanned(result: String?) {
        // Process the scanned result here
        val type = determineQRCodeType(result!!)
        handleQRCodeResult(result, type!!)
    }
    private fun determineQRCodeType(result: String): String? {
        return if (result.startsWith("http://") || result.startsWith("https://")) {
            "URL"
        } else if (result.startsWith("mailto:")) {
            "Email"
        } else if (result.startsWith("tel:")) {
            "Phone Number"
        } else if (result.startsWith("sms:")) {
            "SMS"
        } else if (result.startsWith("geo:")) {
            "Location"
        } else {
            "Text"
        }
    }
    private fun handleQRCodeResult(result: String, type: String) {
        when (type) {
            "URL" -> {
                // Handle URL, e.g., open it in a browser
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
                startActivity(browserIntent)
            }

            "Email" -> {
                // Handle email, e.g., open email client
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(result))
                startActivity(emailIntent)
            }

            "Phone Number" -> {
                // Handle phone number, e.g., open dialer
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse(result))
                startActivity(dialIntent)
            }

            "SMS" -> {
                // Handle SMS, e.g., open SMS app
                val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
                startActivity(smsIntent)
            }

            "Location" -> {
                // Handle location, e.g., open maps
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
                startActivity(mapIntent)
            }

            "Text" ->             // Handle plain text
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()

            else -> Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }
    }

}