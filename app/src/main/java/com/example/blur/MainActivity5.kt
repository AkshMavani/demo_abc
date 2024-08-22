package com.example.blur

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.abc.BottomSheetDialog
import com.example.demo_full.Model_Img
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain5Binding
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.StickerView


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
            Log.e("arr1", "onCreate:>>>$arr1 ", )
            val click=object : Click_Blur {
                override fun blur(modelImg: Model_Img, position: Int) {
                    Glide.with(this@MainActivity5)
                        .load(modelImg.str)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(this@MainActivity5,25f)))
                        .into(binding.imgBlur) // imageView is your ImageView where you want to display the blurred
                }

            }
            var adapter= Adapter_BlurImage(imgUrlList,this,click)
            binding.recuycleViewBlur.adapter=adapter

        }
        binding.btnTwo.setOnClickListener {
            val dialog= Dialog(this, R.style.CustomAlertDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alart_dialog)
//            val img=dialog.findViewById<ImageView>(Build.VERSION_CODES.R.id.btncc)
//            img.setOnClickListener {
//                dialog.cancel()
//            }
            val width = (resources.displayMetrics.widthPixels * 0.93).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.27).toInt()
            dialog.window?.setLayout(width, height)
            dialog.show()
        }
        binding.btnThree.setOnClickListener {
            val bottomSheet = BottomSheetDialog()
            bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
        }

    }

}