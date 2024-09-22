package com.example.abc

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.databinding.ActivitySensorBinding

class activity_sensor : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivitySensorBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private var azimuth = 0f
    private var filteredAzimuth = 0f

    // Smoothing factor for azimuth changes (adjust to control rotation stability)
    private val alpha = 0.1f // Lower alpha for more stability (less movement)

    // Thresholds for detecting rotation close to ±90 degrees
    private val ninetyDegreeThreshold = 10f // The range around 90 degrees to detect rotation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SensorManager and sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Handle accelerometer data
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity[0] = event.values[0]
            gravity[1] = event.values[1]
            gravity[2] = event.values[2]
        }
        // Handle magnetometer data
        else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic[0] = event.values[0]
            geomagnetic[1] = event.values[1]
            geomagnetic[2] = event.values[2]
        }

        // Calculate orientation based on accelerometer and magnetometer data
        val R = FloatArray(9)
        val I = FloatArray(9)
        val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)

        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)
            val newAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()

            // Apply a low-pass filter to smooth the azimuth changes
            filteredAzimuth = alpha * newAzimuth + (1 - alpha) * filteredAzimuth

            // Rotate the image based on the filtered azimuth
            binding.imageView2.rotation = filteredAzimuth

            // Check if the device is close to 90 or -90 degrees and change tint
            if (isNearNinetyDegrees(filteredAzimuth)) {
                // Change image tint to blue when azimuth is near ±90 degrees
                binding.imageView2.setColorFilter(Color.BLUE)
            } else {
                // Reset image tint to default (no tint) when not near ±90 degrees
                binding.imageView2.clearColorFilter()
            }
        }
    }

    // Helper function to check if the azimuth is close to ±90 degrees
    private fun isNearNinetyDegrees(azimuth: Float): Boolean {
        return (azimuth > 90 - ninetyDegreeThreshold && azimuth < 90 + ninetyDegreeThreshold) ||
                (azimuth > -90 - ninetyDegreeThreshold && azimuth < -90 + ninetyDegreeThreshold)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }
}
