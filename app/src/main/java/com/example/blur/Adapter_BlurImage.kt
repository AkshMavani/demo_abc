package com.example.blur

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.demo_full.Model_Img
import com.example.demo_full.R


class Adapter_BlurImage(private val mList: ArrayList<Model_Img>, var context: Context,var clickBlur: Click_Blur) : RecyclerView.Adapter<Adapter_BlurImage.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        Log.e("ITEM", "onBindViewHolder: >>$ItemsViewModel", )
        Glide.with(context)
            .load(ItemsViewModel.str)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(context,25f)))
            .into(holder.imageView) // imageView is your ImageView where you want to display the blurred
        holder.itemView.setOnClickListener {
            clickBlur.blur(ItemsViewModel,position)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_blur_layout)

    }


}