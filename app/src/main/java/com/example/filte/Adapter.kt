package com.example.filte

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R


// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
class Adapter    // Constructor for initialization
    (var context: Context, var images: ArrayList<Int>) :
    RecyclerView.Adapter<Adapter.ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        // Inflating the Layout(Instantiates list_item.xml layout file into View object)
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_new, parent, false)

        // Passing view to ViewHolder
        return ViewHolder(view)
    }

    // Binding data to the into specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TypeCast Object to int type
        val res = images[position] as Int
        holder.images.setImageResource(res)
    }

    override fun getItemCount(): Int {
        // Returns number of items currently available in Adapter
        return images.size
    }

    // Initializing the Views
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var images: ImageView

        init {
            images = view.findViewById<View>(R.id.imageView) as ImageView
        }
    }
}