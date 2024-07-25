package com.example.demo_full

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.filter.Filters
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import java.io.ByteArrayOutputStream

class MainActivity2 : AppCompatActivity() {
    var reciver:BroadcastReceiver?=null
    private lateinit var volumeChangeReceiver: VolumeChangeReceiver
    companion object {
        private val LOG = CameraLogger.create("DemoApp")
        private const val USE_FRAME_PROCESSOR = false
        private const val DECODE_BITMAP = false
    }

    private val camera: CameraView by lazy { findViewById(R.id.camera) }
    private val controlPanel: ImageView by lazy { findViewById(R.id.controls) }
    private var captureTime: Long = 0

    private var currentFilter = 0
    private val allFilters = Filters.values()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        camera.addCameraListener(Listener())
        camera.setLifecycleOwner(this)
        controlPanel.setOnClickListener { val intent=Intent(this,MainActivity3::class.java)
        startActivity(intent)}
        if (USE_FRAME_PROCESSOR) {
            camera.addFrameProcessor(object : FrameProcessor {
                private var lastTime = System.currentTimeMillis()
                override fun process(frame: Frame) {
                    val newTime = frame.time
                    val delay = newTime - lastTime
                    lastTime = newTime
                    LOG.v("Frame delayMillis:", delay, "FPS:", 1000 / delay)
                    if (DECODE_BITMAP) {
                        if (frame.format == ImageFormat.NV21
                            && frame.dataClass == ByteArray::class.java
                        ) {
                            val data = frame.getData<ByteArray>()
                            val yuvImage = YuvImage(
                                data,
                                frame.format,
                                frame.size.width,
                                frame.size.height,
                                null
                            )
                            val jpegStream = ByteArrayOutputStream()
                            yuvImage.compressToJpeg(
                                Rect(
                                    0, 0,
                                    frame.size.width,
                                    frame.size.height
                                ), 100, jpegStream
                            )
                            val jpegByteArray = jpegStream.toByteArray()
                            val bitmap = BitmapFactory.decodeByteArray(
                                jpegByteArray,
                                0, jpegByteArray.size
                            )
                            bitmap.toString()
                        }
                    }
                }
            })

        }



    }

    private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
                Log.e("TAG123456", "onCameraOpened:${options.exposureCorrectionMaxValue} ",)
                Log.e("TAG123456", "onCameraOpened:${options.exposureCorrectionMinValue} ",)
                Log.e("TAG123456", "onCameraOpened:${options.previewFrameRateMaxValue} ",)
                Log.e("TAG123456", "onCameraOpened:${options.previewFrameRateMinValue} ",)
                Log.e("TAG123456", "onCameraOpened:${options.isZoomSupported} ",)
                Log.e("TAG123456", "onCameraOpened:${options.supportedFacing} ",)

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(reciver)
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val sharepref=getSharedPreferences("PREF", MODE_PRIVATE)
        val vlue=sharepref.getBoolean("VOLUMEUP",false)
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    Log.e("TAGzzz", "dispatchKeyEvent----UP: >>$vlue", )
                    if (vlue){
                        Log.e("TAGzzz", "dispatchKeyEvent----UP: ", )
                        camera.takePicture()
                    }

                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    if (vlue){
                        Log.e("TAGzzz", "dispatchKeyEvent----UP: ", )
                        camera.takePicture()
                    }
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }


}