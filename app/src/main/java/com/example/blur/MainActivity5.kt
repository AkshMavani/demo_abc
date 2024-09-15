package com.example.blur

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.abc.FILE
import com.example.demo_full.Model_Img
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain5Binding
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.StickerUtils
import com.xiaopo.flying.sticker.StickerUtils.saveImageToGallery
import com.xiaopo.flying.sticker.StickerView
import java.io.File
import java.io.IOException


class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding


    var arr1:ArrayList<String> = ArrayList()

    var count=0
    var HorizontalLayout: LinearLayoutManager? = null
    var sticker:StickerView?=null
    companion object{
        var color_code:String?=null

    }
    lateinit var imgUrlList:ArrayList<Model_Img>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)


         imgUrlList = intent.getSerializableExtra("img_url") as ArrayList<Model_Img>
        Log.e("IMG09", "onCreate:>>>$imgUrlList ")

        for (i in  imgUrlList){
            if (imgUrlList.size==1){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,true,false,false,false,false,false,false,false,false,"STICKER")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==2){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,true,false,false,false,false,false,false,false,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==3){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,true,false,false,false,false,false,false,"STICKER")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==4){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,true,false,false,false,false,false,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==5){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,true,false,false,false,false,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==6){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,true,false,false,false,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==7){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,true,false,false,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==8){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,false,true,false,"STICKER")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }else if(imgUrlList.size==9){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,false,false,true,"IMG")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }
        binding.btnOne.setOnClickListener {
            binding.recuycleViewBlur.visibility= View.VISIBLE
            HorizontalLayout = LinearLayoutManager(
                this@MainActivity5,
                LinearLayoutManager.HORIZONTAL,
                false
            )
           binding.recuycleViewBlur.setLayoutManager(HorizontalLayout)
            Log.e("arr1", "onCreate: "+ imgUrlList )
            Log.e("arr1", "onCreate:>>>$arr1 ")
            val click=object : Click_Blur {
                override fun blur(modelImg: Model_Img, position: Int) {
//                    Glide.with(this@MainActivity5)
//                        .load(modelImg.str)
//                        .apply(RequestOptions.bitmapTransform(BlurTransformation(this@MainActivity5,25f)))
//                        .into(binding.imgBlur) // imageView is your ImageView where you want to display the blurred
                }

            }
            var adapter= Adapter_BlurImage(imgUrlList,this,click)
            binding.recuycleViewBlur.adapter=adapter

        }
        binding.btnTwo.setOnClickListener {
            binding.stickerView.visibility=View.VISIBLE
            binding.imgBg.setImageResource(R.drawable.image_frame_7)
//            val dialog= Dialog(this, R.style.CustomAlertDialog)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.alart_dialog)
////            val img=dialog.findViewById<ImageView>(Build.VERSION_CODES.R.id.btncc)
////            img.setOnClickListener {
////                dialog.cancel()
////            }
//            val width = (resources.displayMetrics.widthPixels * 0.93).toInt()
//            val height = (resources.displayMetrics.heightPixels * 0.27).toInt()
//            dialog.window?.setLayout(width, height)
//            dialog.show()

        }
        binding.btnThree.setOnClickListener {
//            val file: File = FILE.getNewFile(this@MainActivity5, "Sticker")
//            if (file != null) {
//                // Save the sticker to file
//                //binding.stickerView.save(file)
//                saveImageToGallery(file, binding.stickerView.createBitmap())
//                StickerUtils.notifySystemGallery(this, file)
//                // Set the saved image as the background of the ImageView using Glide
//
//
//                Toast.makeText(this@MainActivity5, "Saved in " + file.absolutePath, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this@MainActivity5, "The file is null", Toast.LENGTH_SHORT).show()
//            }
            binding.stickerView.visibility=View.GONE
            binding.img1.visibility=View.VISIBLE

            binding.img1.setImageBitmap(binding.stickerView.createBitmap())
           saveLayoutAsImage()
            //saveImageToGallery(binding.stickerView.createBitmap(),"hii")
        }
    }


    private fun saveLayoutAsImage() {
        // Get the ConstraintLayout view that you want to capture
        val layout = binding.coordinatorLayout

        // Ensure the layout is fully measured and rendered
        layout.post {
            // Create a bitmap with the same size as the layout
            val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)

            // Create a canvas using the bitmap and draw the layout on the canvas
            val canvas = Canvas(bitmap)
            layout.draw(canvas)

            // Save the bitmap to the gallery
            saveImageToGallery(bitmap, "LayoutImage_${System.currentTimeMillis()}.jpg")
        }
    }

    // Save the bitmap to the gallery
    private fun saveImageToGallery(bitmap: Bitmap, imageName: String) {
        val resolver: ContentResolver = this.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageName) // Name of the image
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg") // Image type (JPEG format)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Directory to save the image
        }

        // Insert the content values into the MediaStore to save it to the gallery
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            // Open an output stream for the inserted Uri
            val fos = resolver.openOutputStream(imageUri!!)

            // Compress the bitmap and write it to the output stream as JPEG
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)
            fos.flush()
            fos.close()

            // Notify the user
            Toast.makeText(this, "Layout saved to gallery!", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save layout: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



}