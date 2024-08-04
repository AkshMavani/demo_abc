package com.example.demo_full

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.demo_full.databinding.ActivityMain5Binding
import com.xiaopo.flying.sticker.BitmapStickerIcon
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.Sticker
import com.xiaopo.flying.sticker.StickerView
import java.io.Serializable

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding
    var arr:ArrayList<Serializable> = ArrayList()
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUrlList = intent.getSerializableExtra("img_url") as ArrayList<Model_Img>
        Log.e("IMG09", "onCreate:>>>$imgUrlList ", )
        for (i in  imgUrlList){
            if (imgUrlList.size==1){
                Glide.with(this)
                    .load(i.str)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val sticker = DrawableSticker(resource)
                            binding.stickerView.addSticker(sticker,300,500,true,false,false,false,false,false,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,true,false,false,false,false,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,true,false,false,false,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,true,false,false,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,true,false,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,true,false,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,true,false,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,false,true,false)
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
                            binding.stickerView.addSticker(sticker,300,500,false,false,false,false,false,false,false,false,true)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }




    }

}