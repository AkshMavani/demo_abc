package com.example.blur


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain13Binding
import java.io.File


class MainActivity13 : AppCompatActivity() {
    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var toggleButton: Button
    private lateinit var binding: ActivityMain13Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain13Binding.inflate(layoutInflater)
        setContentView(binding.root)

        layout1 = findViewById(R.id.layout1)
        layout2 = findViewById(R.id.layout2)
        toggleButton = findViewById(R.id.toggle_button)

        toggleButton.setOnClickListener { toggleLayouts() }

//        val folderList = getImageFoldersWithLastImage(this)
//        Log.e("FOLDER_LIST", "onCreate: >>>>>>$folderList")
//
//        binding.rc.layoutManager = LinearLayoutManager(this)
//
//        val click = object : foulder_click {
//            override fun click(name: Model_Foulder) {
//                val intent = Intent(this@MainActivity13, MainActivity14::class.java)
//                intent.putExtra("folder_path", name.path)
//                startActivity(intent)
//            }
//        }
//
//        // Pass the list of Model_Foulder objects to the FolderAdapter
//        val folderAdapter = FolderAdapter(folderList, this, click)
//        binding.rc.adapter = folderAdapter


    }

    // Updated function to return a list of Model_Foulder objects
    fun getImageFoldersWithLastImage(context: Context): List<Model_Foulder> {
        val folderList: MutableList<Model_Foulder> = mutableListOf()
        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED)

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        )

        if (cursor != null) {
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(dataColumn)
                val file = File(imagePath)
                val folderPath = file.parent

                // If folderPath is empty or null, assign "Other" as default folder name
                val folderName = if (!folderPath.isNullOrEmpty()) {
                    File(folderPath).name
                } else {
                    "Other"
                }

                // Only add the folder if it's not already in the list
                if (folderList.none { it.path == folderPath }) {
                    folderList.add(Model_Foulder(folderName, imagePath, folderPath ?: "Other"))
                }
            }
            cursor.close()
        }
        return folderList
    }

    private fun toggleLayouts() {
        if (layout1.visibility == View.VISIBLE) {
            // Slide down layout1 and slide up layout2
            slideDown(layout1)
            slideUp(layout2)
            layout1.visibility = View.GONE
            layout2.visibility = View.VISIBLE
        } else {
            // Slide up layout1 and slide down layout2
            slideUp(layout1)
            slideDown(layout2)
            layout1.visibility = View.VISIBLE
            layout2.visibility = View.GONE
        }
    }

    private fun slideUp(view: View) {
        view.animate()
            .translationY(-view.height.toFloat())
            .alpha(0.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.VISIBLE
                }
            })
    }

    private fun slideDown(view: View) {
        view.animate()
            .translationY(view.height.toFloat())
            .alpha(1.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                }
            })
    }
}

