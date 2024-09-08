package com.example.abc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.frame.Frame

class Activity_Camera : AppCompatActivity() {
    companion object {
        private val LOG = CameraLogger.create("DemoApp")
        private const val USE_FRAME_PROCESSOR = false
        private const val DECODE_BITMAP = false
    }
//
    private val camera: CameraView by lazy { findViewById(R.id.cameraView1) }
    private val camera2: CameraView by lazy { findViewById(R.id.cameraView2) }
    private val camera3: CameraView by lazy { findViewById(R.id.cameraView3) }
    private val camera4: CameraView by lazy { findViewById(R.id.cameraView4) }
    private val camera5: CameraView by lazy { findViewById(R.id.cameraView5) }
    private val camera6: CameraView by lazy { findViewById(R.id.cameraView6) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var gestureDetector: GestureDetector
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        camera.setLifecycleOwner(this)

        camera.addFrameProcessor { frame ->
            Log.e("TAG12", "onCreate:>>>${frame.format} ")
            Log.e("TAG12", "onCreate:>>>${frame.rotation} ")

        }
        // Initialize GestureDetector
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                // Display Toast when CameraView is clicked
                Toast.makeText(this@Activity_Camera, "CameraView clicked!", Toast.LENGTH_SHORT).show()
                return super.onSingleTapConfirmed(e)
            }
        })
        camera.setOnTouchListener(View.OnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        })

//        fun onPictureTaken(result: PictureResult) {
//            super.onPictureTaken(result)
//            Log.e("TAG_CAMERA", "onPictureTaken: $result")
//            //            Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);
////            Bitmap croppedBitmap = cropToAspectRatio(bitmap, 3 / 4f);
////            activityMain3Binding.camera.setVisibility(View.GONE);
////            activityMain3Binding.img.setVisibility(View.VISIBLE);
////            activityMain3Binding.img.setImageBitmap(croppedBitmap);
//            val bitmap = BitmapFactory.decodeByteArray(result.data, 0, result.data.size)
//
//            // Rotate the bitmap to portrait if necessary
//            val rotatedBitmap = rotateToPortrait(bitmap)
//
//            // Crop the bitmap to the 16:9 aspect ratio
//            val croppedBitmap = cropToAspectRatio(rotatedBitmap!!, 16f / 9f)
//            activityMain3Binding.camera.setVisibility(View.GONE)
//            activityMain3Binding.img.setVisibility(View.VISIBLE)
//            activityMain3Binding.img.setImageBitmap(croppedBitmap)
//        }
    }
    private fun Frame.toBitmap(): Bitmap? {
        // Convert frame to Bitmap (implementation depends on the frame format)
        return null
    }

    private fun divideBitmap(bitmap: Bitmap): Pair<Bitmap, Bitmap> {
        val width = bitmap.width
        val height = bitmap.height

        val leftBitmap = Bitmap.createBitmap(bitmap, 0, 0, width / 2, height)
        val rightBitmap = Bitmap.createBitmap(bitmap, width / 2, 0, width / 2, height)

        return Pair(leftBitmap, rightBitmap)
    }

    private fun updateUIWithDividedBitmaps(bitmaps: Pair<Bitmap, Bitmap>) {

    }
    fun cropToAspectRatio(bitmap: Bitmap, aspectRatio: Float): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height

        // Calculate the target width and height based on the aspect ratio
        val targetWidth: Int
        val targetHeight: Int
        if (width.toFloat() / height > aspectRatio) {
            targetHeight = height
            targetWidth = (height * aspectRatio).toInt()
        } else {
            targetWidth = width
            targetHeight = (width / aspectRatio).toInt()
        }

        // Calculate the starting points for cropping
        val xOffset = (width - targetWidth) / 2
        val yOffset = (height - targetHeight) / 2

        // Crop the bitmap to the desired aspect ratio
        return Bitmap.createBitmap(bitmap, xOffset, yOffset, targetWidth, targetHeight)
    }

    fun rotateToPortrait(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()

        // Assuming 90-degree rotation for portrait (adjust if necessary)
        matrix.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}

//package com.example.twitterlogin
//import com.example.twitterlogin.databinding.ActivityMain3Binding
//
//class MainActivity3 : AppCompatActivity() {
//    private var activityMain3Binding: ActivityMain3Binding? = null
//    var flag = false
//    var value = 0f
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val gestureDetector: GestureDetector
//        activityMain3Binding = ActivityMain3Binding.inflate(layoutInflater)
//        setContentView(activityMain3Binding.getRoot())
//        activityMain3Binding.camera.addCameraListener(com.example.twitterlogin.MainActivity3.Listener())
//        activityMain3Binding.camera.setLifecycleOwner(this)
//        activityMain3Binding.camera.setOnClickListener(View.OnClickListener { // Display Toast when CameraView is clicked
//            Toast.makeText(this@MainActivity3, "CameraView clicked!", Toast.LENGTH_SHORT).show()
//        })
//        // Initialize GestureDetector
//        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
//            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
//                // Display Toast when CameraView is clicked
//                Toast.makeText(this@MainActivity3, "CameraView clicked!", Toast.LENGTH_SHORT).show()
//                return super.onSingleTapConfirmed(e)
//            }
//        })
//        activityMain3Binding.camera.setOnTouchListener(View.OnTouchListener { v, event ->
//            gestureDetector.onTouchEvent(event)
//            true
//        })
//        activityMain3Binding.capture.setOnClickListener(View.OnClickListener { capturePicture() })
//        activityMain3Binding.video.setOnClickListener(View.OnClickListener { // activityMain3Binding.camera.setMode(Mode.VIDEO);
//            captureVideo()
//        })
//        activityMain3Binding.front.setOnClickListener(View.OnClickListener {
//            flag = !flag
//            if (flag) {
//                activityMain3Binding.camera.setFacing(Facing.FRONT)
//            } else {
//                activityMain3Binding.camera.setFacing(Facing.BACK)
//            }
//        })
//        activityMain3Binding.stopvideo.setOnClickListener(View.OnClickListener { activityMain3Binding.camera.stopVideo() })
//        activityMain3Binding.seek.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                value = getConvertedValue(progress)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                activityMain3Binding.camera.setZoom(value)
//            }
//        })
//    }
//
//    private inner class Listener : CameraListener() {
//        override fun onCameraOpened(options: CameraOptions) {}
//        override fun onCameraError(exception: CameraException) {
//            super.onCameraError(exception)
//            // message("Got CameraException #" + exception.getReason(), true);
//        }
//
//        override fun onPictureTaken(result: PictureResult) {
//            super.onPictureTaken(result)
//            Log.e("TAG_CAMERA", "onPictureTaken: $result")
//            //            Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);
////            Bitmap croppedBitmap = cropToAspectRatio(bitmap, 3 / 4f);
////            activityMain3Binding.camera.setVisibility(View.GONE);
////            activityMain3Binding.img.setVisibility(View.VISIBLE);
////            activityMain3Binding.img.setImageBitmap(croppedBitmap);
//            val bitmap = BitmapFactory.decodeByteArray(result.data, 0, result.data.size)
//
//            // Rotate the bitmap to portrait if necessary
//            val rotatedBitmap = rotateToPortrait(bitmap)
//
//            // Crop the bitmap to the 16:9 aspect ratio
//            val croppedBitmap = cropToAspectRatio(rotatedBitmap, 16f / 9f)
//            activityMain3Binding.camera.setVisibility(View.GONE)
//            activityMain3Binding.img.setVisibility(View.VISIBLE)
//            activityMain3Binding.img.setImageBitmap(croppedBitmap)
//        }
//
//        override fun onVideoTaken(result: VideoResult) {
//            super.onVideoTaken(result)
//            Log.e("TAG_CAMERA", "videoTaken: $result")
//        }
//
//        override fun onVideoRecordingStart() {
//            super.onVideoRecordingStart()
//        }
//
//        override fun onVideoRecordingEnd() {
//            super.onVideoRecordingEnd()
//        }
//
//        override fun onExposureCorrectionChanged(
//            newValue: Float,
//            bounds: FloatArray,
//            fingers: Array<PointF>?,
//        ) {
//            super.onExposureCorrectionChanged(newValue, bounds, fingers)
//        }
//
//        override fun onZoomChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
//            super.onZoomChanged(newValue, bounds, fingers)
//            Log.e("newVALUE", "onZoomChanged: $newValue")
//        }
//    }
//
//    private fun captureVideo() {
//        activityMain3Binding.camera.setMode(Mode.VIDEO)
//        if (activityMain3Binding.camera.getMode() === Mode.PICTURE) {
//            Toast.makeText(
//                this@MainActivity3,
//                "Can't record HQ videos while in PICTURE mode",
//                Toast.LENGTH_SHORT
//            ).show()
//            // message("Can't record HQ videos while in PICTURE mode.", false);
//            return
//        }
//        if (activityMain3Binding.camera.isTakingPicture() || activityMain3Binding.camera.isTakingVideo()) {
//            return
//        }
//        // message("Recording for 5 seconds...", true);
//        activityMain3Binding.camera.takeVideo(File(filesDir, "video.mp4"), 5000)
//    }
//
//    private fun capturePicture() {
//        activityMain3Binding.camera.setMode(Mode.PICTURE)
//        if (activityMain3Binding.camera.getMode() === Mode.VIDEO) {
//            Toast.makeText(
//                this@MainActivity3,
//                "Can't take HQ pictures while in VIDEO mode",
//                Toast.LENGTH_SHORT
//            ).show()
//            // message("Can't take HQ pictures while in VIDEO mode.", false);
//            return
//        }
//        if (activityMain3Binding.camera.isTakingPicture()) {
//            return
//        }
//        activityMain3Binding.camera.takePicture()
//    }
//
//    fun getConvertedValue(intVal: Int): Float {
//        var floatVal = 0.0f
//        floatVal = .5f * intVal
//        return floatVal
//    }
//
//    fun cropToAspectRatio(bitmap: Bitmap, aspectRatio: Float): Bitmap {
//        val width = bitmap.width
//        val height = bitmap.height
//
//        // Calculate the target width and height based on the aspect ratio
//        val targetWidth: Int
//        val targetHeight: Int
//        if (width.toFloat() / height > aspectRatio) {
//            targetHeight = height
//            targetWidth = (height * aspectRatio).toInt()
//        } else {
//            targetWidth = width
//            targetHeight = (width / aspectRatio).toInt()
//        }
//
//        // Calculate the starting points for cropping
//        val xOffset = (width - targetWidth) / 2
//        val yOffset = (height - targetHeight) / 2
//
//        // Crop the bitmap to the desired aspect ratio
//        return Bitmap.createBitmap(bitmap, xOffset, yOffset, targetWidth, targetHeight)
//    }
//
//    fun rotateToPortrait(bitmap: Bitmap): Bitmap {
//        val matrix = Matrix()
//
//        // Assuming 90-degree rotation for portrait (adjust if necessary)
//        matrix.postRotate(90f)
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }
//}