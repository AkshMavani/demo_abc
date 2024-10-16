package com.example.demo_full

import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field


class activity_popup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
        val btn=findViewById<Button>(R.id.button2)
        btn.setOnClickListener {click->
            val popupMenu = PopupMenu(this@activity_popup, click)

            // Inflate the custom menu
            popupMenu.menuInflater.inflate(R.menu.main12, popupMenu.menu)

            // Apply style to show icons
            try {
                val mPopup: Field = PopupMenu::class.java.getDeclaredField("mPopup")
                mPopup.isAccessible = true
                val menuPopupHelper = mPopup.get(popupMenu)
                val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                setForceIcons.invoke(menuPopupHelper, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Show the popup
            popupMenu.show()


        }
    }
}