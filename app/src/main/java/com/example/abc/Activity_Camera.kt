package com.example.abc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.camera.MainActivity
import com.example.demo_full.R
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import java.io.ByteArrayOutputStream

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
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        camera.setLifecycleOwner(this)

        camera.addFrameProcessor { frame ->
            Log.e("TAG12", "onCreate:>>>${frame.format} ",)
            Log.e("TAG12", "onCreate:>>>${frame.rotation} ",)

        }

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

}