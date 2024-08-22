package com.example.abc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.demo_full.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        val inbox = view.findViewById<TextView>(R.id.TvInbox)
        val document = view.findViewById<TextView>(R.id.TvDocumet)
        val camera = view.findViewById<TextView>(R.id.TvCamera0)
        val gallery = view.findViewById<TextView>(R.id.TvGallery0)
        val googleplus = view.findViewById<TextView>(R.id.TvGoogleplus)

        inbox.setOnClickListener {
            Toast.makeText(activity, " Inbox", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        document.setOnClickListener {
            Toast.makeText(activity, " Document", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        camera.setOnClickListener {
            Toast.makeText(activity, " camera", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        gallery.setOnClickListener {
            Toast.makeText(activity, " gallery", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        googleplus.setOnClickListener {
            Toast.makeText(activity, " googleplus", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return view
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

}
