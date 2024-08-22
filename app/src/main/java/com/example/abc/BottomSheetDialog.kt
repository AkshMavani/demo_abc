package com.example.abc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blur.Adapter_StickerView
import com.example.demo_full.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog : BottomSheetDialogFragment() {
    @SuppressLint("MissingInflatedId")
    var arr:ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        val recyclerView:RecyclerView = view.findViewById<RecyclerView>(R.id.Tvopen)
        val layoutManager = GridLayoutManager(context, 4)

        recyclerView.layoutManager = layoutManager
        arr.add(str)
        arr.add(str1)
        arr.add(str2)
        arr.add(str3)
        arr.add(str4)
        arr.add(str5)
        arr.add(str6)
        arr.add(str7)
        val adapter= context?.let { Adapter_StickerView(arr, it) }
        recyclerView.adapter=adapter

        return view
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
   companion object{
       var str="/storage/emulated/0/Pictures/IMG_20240818_191840_1.jpg"
       var str1="/storage/emulated/0/Pictures/IMG_20240818_191840.jpg"
       var str2="/storage/emulated/0/Pictures/IMG_20240818_191840.jpg"
       var str3="/storage/emulated/0/Pictures/IMG_20240818_191839.jpg"
       var str4="/storage/emulated/0/Pictures/IMG_20240818_191840_1.jpg"
       var str5="/storage/emulated/0/Pictures/IMG_20240818_191840.jpg"
       var str6="/storage/emulated/0/Pictures/IMG_20240818_191840.jpg"
       var str7="/storage/emulated/0/Pictures/IMG_20240818_191839.jpg"
   }
}
