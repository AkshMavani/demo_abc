package com.example.gallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain11Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity11 : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding:ActivityMain11Binding
    var navView: BottomNavigationView? = null
    var view:View?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain11Binding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = findViewById(R.id.nav_view) as BottomNavigationView
        view=findViewById(R.id.in_multi_select) as View
      //  navView!!.getMenu().findItem(R.id.navigation_home).setChecked(true);
        // Set up NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }

    fun displayDelete() {
        binding.inMultiSelect.getRoot().setVisibility(View.VISIBLE)
    }

    fun hideDelete() {
        binding.inMultiSelect.root.visibility = View.GONE
    }

}