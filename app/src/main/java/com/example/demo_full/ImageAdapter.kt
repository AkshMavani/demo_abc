package com.example.demo_full


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abc.ImageAdapter
import java.io.Serializable


public class ImageAdapter12(private val context: Context, private val imagePaths: ArrayList<Model_Img>) :
    RecyclerView.Adapter<ImageAdapter12.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(imagePaths[position].str).into(holder.imageView)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,MainActivity_data::class.java)
            intent.putExtra("imgList",imagePaths as Serializable)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return imagePaths.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.imageView)
        }
    }
}