package com.example.blur

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo_full.R

class ImageAdapter1(private var images: List<String>) : RecyclerView.Adapter<ImageAdapter1.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = images[position]
        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(imagePath)
            .into(holder.imageView)

        // You can handle clicks here to open the image in full screen
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewPagerActivity::class.java)
            intent.putStringArrayListExtra("image_list", ArrayList(images))
            intent.putExtra("image_position", position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
