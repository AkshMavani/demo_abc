package com.example.multifit

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R

class HorizontalWheelAdapter123(
    private val context: Context,
    private val items: List<String>,
    private var selectedItemPosition: Int = 2, // Default selected position
    private val onItemSelectedListener: (Int) -> Unit
) : RecyclerView.Adapter<HorizontalWheelAdapter123.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wheel1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
        holder.textView.setTextColor(if (position == selectedItemPosition) Color.YELLOW else Color.BLACK)

        holder.itemView.setOnClickListener {
            val previousSelected = selectedItemPosition
            selectedItemPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedItemPosition)
            onItemSelectedListener(selectedItemPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}