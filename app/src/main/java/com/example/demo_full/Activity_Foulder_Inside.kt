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
import com.example.blur.MainActivity5
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
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.layoutManager = GridLayoutManager(this, 3)


        val images = intent?.let { getMediaInFolder(this, it) }

        val imageAdapter = images?.let { ImageAdapter12(this, images as ArrayList<Model_Img>,this) }
        imageRecyclerView.adapter = imageAdapter
    }

//    fun getMediaInFolder(context: Context, folderName: String): List<Model_Img> {
//        val mediaList = mutableListOf<Model_Img>()
//
//        // Query images
//        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
//        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
//        val selectionArgs = arrayOf(folderName)
//        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
//
//        val imageCursor = context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            imageProjection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )
//        imageCursor?.use {
//            val imagePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            while (it.moveToNext()) {
//                val imagePath = it.getString(imagePathColumn)
//                mediaList.add(Model_Img(imagePath,"IMG"))
//            }
//        }
//        val videoProjection = arrayOf(MediaStore.Video.Media.DATA)
//        val videoCursor = context.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            videoProjection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )
//
//        videoCursor?.use {
//            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
//            while (it.moveToNext()) {
//                val videoPath = it.getString(videoPathColumn)
//                mediaList.add(Model_Img(videoPath,"VIDEO"))
//            }
//        }
//
//        return mediaList
//    }

    fun getMediaInFolder(context: Context, folderName: String): List<Model_Img> {
        val mediaList = ArrayList<Model_Img>()

        // Query for images
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DATA,              // File path
            MediaStore.Images.Media.DATE_TAKEN,        // Date taken
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // Folder name
        )

        val selection = if (folderName == "Unknown") {
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} IS NULL OR ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ''"
        } else {
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        }
        val selectionArgs = if (folderName == "Unknown") null else arrayOf(folderName)
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
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val imagePath = it.getString(imagePathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get date taken for the image
                mediaList.add(Model_Img(imagePath, "IMG", dateTaken))
            }
        }

        // Query for videos
        val videoProjection = arrayOf(
            MediaStore.Video.Media.DATA,              // File path
            MediaStore.Video.Media.DATE_TAKEN,        // Date taken
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME // Folder name
        )

        val videoSelection = if (folderName == "Unknown") {
            "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} IS NULL OR ${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} = ''"
        } else {
            "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} = ?"
        }
        val videoSelectionArgs = if (folderName == "Unknown") null else arrayOf(folderName)

        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            videoSelection,
            videoSelectionArgs,
            sortOrder
        )

        videoCursor?.use {
            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val videoPath = it.getString(videoPathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get date taken for the video
                mediaList.add(Model_Img(videoPath, "VIDEO", dateTaken))
            }
        }

        return mediaList
    }



    @SuppressLint("SuspiciousIndentation")
    override fun click(modelImg: Model_Img) {
        arr.add(modelImg)
        Log.e("img123", "click:${modelImg.str} ")

        binding.btnClick.setOnClickListener {
            Log.e("img123", "click:${arr} ")
            val intent = Intent(this, MainActivity_rc::class.java)
            intent.putExtra("flag", false)
            intent.putExtra("img_url", arr as Serializable)
            startActivity(intent)
        }
        }
    }

