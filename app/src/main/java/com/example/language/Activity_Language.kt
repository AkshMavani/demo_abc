package com.example.language

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R

class Activity_Language : AppCompatActivity() {
    var sharedPreferences: SharedPrefs? = null
    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = SharedPrefs(newBase)
        val languageCode = sharedPreferences!!.locale
        val context: Context = LanguageConfig.changeLanguage(newBase, languageCode)
        super.attachBaseContext(context)
    }
    lateinit var b1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        b1 = findViewById(R.id.b1)
        b1.setOnClickListener(View.OnClickListener {
            if (sharedPreferences!!.locale === "en") {
                sharedPreferences!!.locale = "hi"
            } else {
                sharedPreferences!!.locale = "en"
            }
            this.recreate()
        })
    }
}