package com.example.demo_full

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camera.Click_image
import java.io.File
import java.io.Serializable

class ImageAdapter(private val mList: List<Model_Img>, private val context: Context,var click:Click_image) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the item_image view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPath = mList[position]
        val uri = Uri.fromFile(File(itemPath.str))


            holder.videoView.visibility = View.GONE
            holder.imageView.visibility = View.VISIBLE

            Glide.with(context)
                .load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView)
            holder.itemView.setOnClickListener {
//                val intent=Intent(context,MainActivity_data::class.java)
//                intent.putExtra("flag",false)
//                intent.putExtra("img",itemPath.str)
//                context.startActivity(intent)
                    click.click(itemPath)

        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
    }
}
