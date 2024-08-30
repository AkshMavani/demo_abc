package com.example.abc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.demo_full.databinding.ActivityPanoBinding
import com.example.pano.ArcProgressBar
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID


class Activity_pano : AppCompatActivity() {
    private var arcProgressBar: ArcProgressBar? = null
    lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var cameraPreview: SurfaceView? = null
    private var handler: Handler? = null

    private var lastAzimuth: Float = 0f
    var totalRotation: Float = 0f
    private var isCapturing = false

    private val capturedImages: ArrayList<Bitmap> = ArrayList()
    private lateinit var binding:ActivityPanoBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityPanoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var path="/storage/emulated/0/Pictures/IMG_20240829_213142.jpg"
        var destination=StringBuilder(UUID.randomUUID().toString()).append("jpg").toString()
        val options = UCrop.Options()
        val sourceUri = File(path).toUri()
        val destinationUri = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), destination).toUri()

        UCrop.of(sourceUri, destinationUri)

            .withAspectRatio(1F, 1F)
            .withOptions(options)
            .withMaxResultSize(2000, 2000)
            .start(this);


//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "hello.png")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SavedLayouts")
//        }
//
//        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            val outputStream = contentResolver.openOutputStream(it)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//            outputStream?.close()
//            Toast.makeText(this, "Layout saved successfully!", Toast.LENGTH_SHORT).show()
//        }



//        arcProgressBar = findViewById(R.id.arcProgressBar)
//        cameraPreview = findViewById(R.id.cameraPreview)
//        handler = Handler()
//
//        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

//    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
//        var gravity: FloatArray? = null
//        var geomagnetic: FloatArray? = null
//
//        override fun onSensorChanged(event: SensorEvent) {
//            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) gravity = event.values
//            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) geomagnetic = event.values
//            if (gravity != null && geomagnetic != null) {
//                val R = FloatArray(9)
//                val I = FloatArray(9)
//                val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
//                if (success) {
//                    val orientation = FloatArray(3)
//                    SensorManager.getOrientation(R, orientation)
//                    val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // azimuth
//
//                    // Calculate the difference in rotation
//                    var deltaAzimuth = azimuth - lastAzimuth
//
//                    // Handle rotation wrapping around the -180/180 degree mark
//                    if (deltaAzimuth > 180) {
//                        deltaAzimuth -= 360f
//                    } else if (deltaAzimuth < -180) {
//                        deltaAzimuth += 360f
//                    }
//                    totalRotation += deltaAzimuth
//                    lastAzimuth = azimuth
//
//                    // Map the total rotation to progress (0 to 360 degrees)
//                    var progress = totalRotation / 360 * 360
//
//                    // Clamp progress to avoid overflow
//                    progress = progress.coerceIn(0f, 360f)
//
//                    arcProgressBar!!.setProgress(progress)
//
//                    // Capture image at regular intervals
//                    if (!isCapturing && totalRotation >= 15 && totalRotation <= 360) { // Capture every 15 degrees
//                        isCapturing = true
//                        captureImage()
//                    }
//
//                    // When a full 360° rotation is complete
//                    if (progress >= 360) {
//                        mergeCapturedImages()
//                    }
//                }
//            }
//        }
//
//        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
//    }
//
//    private fun captureImage() {
//        // Simulate capturing an image using the camera (in real scenario, use Camera API or CameraX)
//        handler!!.postDelayed({
//            val capturedImage = captureImageFromPreview() // This is a placeholder function
//            if (capturedImage != null) {
//                capturedImages.add(capturedImage)
//            }
//            isCapturing = false
//        }, 500) // Delay to simulate image capture time
//    }
//
//    private fun captureImageFromPreview(): Bitmap? {
//        // Placeholder for capturing a bitmap from the camera preview
//        // Use CameraX or Camera API to actually capture an image
//        return try {
//            MediaStore.Images.Media.getBitmap(
//                this.contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun mergeCapturedImages() {
//        if (capturedImages.size >= 3) { // Ensure at least 3 images are captured to create a panorama
//            val mergedPanorama = mergeBitmapsHorizontally(capturedImages)
//            mergedPanorama?.let { savePanoramaImage(it) }
//        } else {
//            Toast.makeText(this, "Not enough images to create a panorama.", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//
//    private fun mergeBitmapsHorizontally(bitmaps: List<Bitmap>): Bitmap? {
//        val totalWidth = bitmaps.sumOf { it.width }
//        val maxHeight = bitmaps.maxOf { it.height }
//
//        // Create a new bitmap to hold the panorama
//        val panorama = Bitmap.createBitmap(totalWidth, maxHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(panorama)
//
//        // Draw each bitmap onto the panorama canvas
//        var currentWidth = 0
//        for (bitmap in bitmaps) {
//            canvas.drawBitmap(bitmap, currentWidth.toFloat(), 0f, null)
//            currentWidth += bitmap.width
//        }
//        return panorama
//    }
//
//    private fun savePanoramaImage(panorama: Bitmap) {
//        // Save the panorama image to external storage or display it in an ImageView
//        // Placeholder function for saving the bitmap
//        Toast.makeText(this, "Panorama created successfully!", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        sensorManager.registerListener(
//            sensorEventListener,
//            accelerometer,
//            SensorManager.SENSOR_DELAY_UI
//        )
//        sensorManager.registerListener(
//            sensorEventListener,
//            magnetometer,
//            SensorManager.SENSOR_DELAY_UI
//        )
//    }
//
//    override fun onPause() {
//        super.onPause()
//        sensorManager.unregisterListener(sensorEventListener)
//    }

    fun saveLayoutAsImage(view: View, fileName: String): Boolean {
        // Measure and layout the view
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        // Create a bitmap from the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Save the bitmap to storage
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/SavedLayouts")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val file = File(storageDir, "$fileName.png")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val intent=Intent(this,Activity_pano::class.java)
            intent.putExtra("CROP",resultUri.toString())
            setResult(101,intent)
            Log.e("Result", "onActivityResult$resultUri ", )
            //finish()
            binding.imageView3.setImageURI(resultUri)

                //startActivity(intent)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
        }
    }
}
