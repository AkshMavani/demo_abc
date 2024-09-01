package com.example.demo_full


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abc.activity_video

interface click_img{
    fun click(img:Model_Img)
}

class ImagePagerAdapter_DTA(private val context: Context, private val imageUrls: ArrayList<Model_Img>,var clickImg1: click_img) :
    RecyclerView.Adapter<ImagePagerAdapter_DTA.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.image_item_dta, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        if (imageUrl.type.equals("IMG")){
            holder.imageView.visibility=View.VISIBLE
            holder.videoview.visibility=View.GONE

            Glide.with(context)
                .load(imageUrl.str)
                .into(holder.imageView)

        }else{
            holder.videoview.visibility=View.VISIBLE
            holder.imageView.visibility=View.GONE

//           holder.videoview.setVideoURI(imageUrl.str.toUri())
//
//            // Set MediaController to allow play, pause, forward, etc.
//
//            // Set MediaController to allow play, pause, forward, etc.
//            val mediaController = MediaController(context)
//            mediaController.setAnchorView(holder.videoview)
//            holder.videoview.setMediaController(mediaController)
//            holder.videoview.setOnCompletionListener {
//                // Handle video completion
//                holder.videoview.stopPlayback() // Ensure playback is stopped
//                // Optionally, do something when video completes
//            }
//            holder.videoview.start()
            val intent=Intent(context,activity_video::class.java)
            intent.putExtra("VIDEO_URL",imageUrl.str)
            context.startActivity(intent)

        }
        holder.itemView.setOnClickListener {
            clickImg1.click(imageUrl)
           // imageUrls.removeAt(position)
//            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var videoview: VideoView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.img_dta)
            videoview = itemView.findViewById<VideoView>(R.id.video_dta)
        }
    }
}