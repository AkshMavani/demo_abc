package com.example.demo_full

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.Click_image

import com.example.demo_full.databinding.ActivityMain4Binding
import java.io.Serializable


class MainActivity4 : AppCompatActivity(),Click_image {

    private lateinit var binding: ActivityMain4Binding
    var arr:ArrayList<Model_Img> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent.getStringExtra("Foulder")
        Log.e("FLP", "onCreate:$intent ",)
        val a = getMediaInFolder(this, intent.toString())
        Log.e("FLP", "onCreate:>>>>>>>>>>>>>>>$a ",)
        val imageRecyclerView = findViewById<RecyclerView>(R.id.imageRecyclerView)

        imageRecyclerView.layoutManager = GridLayoutManager(this, 3)


        val images = intent?.let { getMediaInFolder(this, it) }

        val imageAdapter = images?.let { ImageAdapter(images, this,this) }
        imageRecyclerView.adapter = imageAdapter
    }

    fun getMediaInFolder(context: Context, folderName: String): List<Model_Img> {
        val mediaList = mutableListOf<Model_Img>()

        // Query images
        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(folderName)
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            selection,
            selectionArgs,
            sortOrder
        )
        imageCursor?.use {
            val imagePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(imagePathColumn)
                mediaList.add(Model_Img(imagePath))
            }
        }
        val videoProjection = arrayOf(MediaStore.Video.Media.DATA)
        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            selection,
            selectionArgs,
            sortOrder
        )

        videoCursor?.use {
            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (it.moveToNext()) {
                val videoPath = it.getString(videoPathColumn)
                mediaList.add(Model_Img(videoPath))
            }
        }

        return mediaList
    }

    @SuppressLint("SuspiciousIndentation")
    override fun click(modelImg: Model_Img) {
        arr.add(modelImg)
        binding.btnClick.setOnClickListener {
            val intent= Intent(this,MainActivity5::class.java)
                intent.putExtra("flag",false)
                intent.putExtra("img_url",arr as Serializable)
               startActivity(intent)
        }
    }


}
