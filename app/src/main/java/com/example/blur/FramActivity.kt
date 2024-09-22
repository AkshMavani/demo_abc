package com.example.blur

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.yalantis.ucrop.UCrop
import java.io.File


class FramActivity : AppCompatActivity() {
    var code = 0
    var flag_check=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fram)

        val img = intent.getStringExtra("imgList")
        if (img != null) {
            val file = File(img)
            val uri = Uri.fromFile(file)
            Log.d("FramActivity", "Image URI: $uri")
            startCrop(uri)
        } else {
            Log.e("FramActivity", "Image path is null!")
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationFileName = "cropped_image.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))
        flag_check=true
        UCrop.of(uri, destinationUri)
            .withAspectRatio(16f, 9f)
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        code = resultCode
        Log.e("CODE12", "onActivityResult: #$code")
        Log.e("CODE123", "reqCode: #$requestCode")
        Log.e("CODE123", "resCode: #$resultCode")
        if (requestCode == 69 ) {
            if (flag_check) {
                showExitDialog();

            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)

            if (resultUri != null) {
                Log.d("FramActivity", "Cropped image URI: $resultUri")
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
            Log.e("FramActivity", "Crop error: ${cropError?.message}")
        }
    }


    private fun showExitDialog() {
        // Create an AlertDialog to confirm the user's intent to exit
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you really want to exit?")
        builder.setCancelable(false) // Prevent the dialog from being dismissed by touching outside

        // 'Yes' option: Exit the activity and return a result
        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            setResult(RESULT_CANCELED)  // Set a result code (optional)
            finish()  // Finish the activity (exit) when "Yes" is pressed
        }

        // 'No' option: Just dismiss the dialog and keep the activity running
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss() // Dismiss the dialog and do nothing else
        }

        // Create and show the dialog
        val alert = builder.create()
        alert.show()
    }

}
