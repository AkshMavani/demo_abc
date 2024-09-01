package com.example.language

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abc.Activity_Camera1
import com.example.demo_full.R
import com.example.multifit.OnboardingExample1Activity
import java.util.Locale


class Activity_Language : AppCompatActivity() {
//    var sharedPreferences: SharedPrefs? = null
//    override fun attachBaseContext(newBase: Context?) {
//        sharedPreferences = SharedPrefs(newBase)
//        val languageCode = sharedPreferences!!.locale
//        val context: Context = LanguageConfig.changeLanguage(newBase, languageCode)
//        super.attachBaseContext(context)
//    }
    lateinit var b1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
//        b1 = findViewById(R.id.b1)
        val recyclerView=findViewById<RecyclerView>(R.id.recycleview_language)
//        b1.setOnClickListener(View.OnClickListener {
//            setLocale(this, "hi")
//            recreate()
//            val intent=Intent(this,OnboardingExample1Activity::class.java)
//            startActivity(intent)
//        })

        var click=object :click_language{
            override fun click(position: Int, name: String) {
                setLocale(this@Activity_Language, "hi")
                recreate()
                val intent=Intent(this@Activity_Language,Activity_Camera1::class.java)
                startActivity(intent)
            }

        }

        val layoutManager = GridLayoutManager(this, 2)

        // at last set adapter to recycler view.

        // at last set adapter to recycler view.
        Log.e("TAG", "onCreate:${list.arr} ", )
        val adapter = Language_Adapter(list.setlnguage() as ArrayList<Model_Language>,click)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        val intent=Intent(this,Activity_Camera1::class.java)
        startActivity(intent)
    }

}

object list{

    var arr:ArrayList<Model_Language>  = ArrayList()
    fun setlnguage():List<Model_Language>{
        arr.clear()
        arr.add(Model_Language(R.drawable.ic_launcher_round,"Hindi"))
        arr.add(Model_Language(R.drawable.ic_launcher_round,"Eng"))
        arr.add(Model_Language(R.drawable.ic_launcher_round,"Franch"))
        arr.add(Model_Language(R.drawable.ic_launcher_round,"Guj"))
        return arr
    }
}