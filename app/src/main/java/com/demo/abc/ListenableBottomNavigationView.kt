package com.demo.abc


import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener

class ListenableBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr), OnNavigationItemSelectedListener {

    private val onNavigationItemSelectedListeners = mutableListOf<OnNavigationItemSelectedListener>()

    init {
        super.setOnNavigationItemSelectedListener(this)
    }

    // Override to allow adding listeners
    override fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        listener?.let { addOnNavigationItemSelectedListener(it) }
    }

    // Add listener to the list
    fun addOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        onNavigationItemSelectedListeners.add(listener)
    }

    // Add listener with a lambda function that accepts the item index
    fun addOnNavigationItemSelectedListener(listener: (Int) -> Unit) {
        addOnNavigationItemSelectedListener(OnNavigationItemSelectedListener { item ->
            // Get the index of the selected menu item
            val index = (0 until menu.size()).indexOfFirst { menu.getItem(it) == item }

            if (index != -1) listener(index)
            false
        })
    }

    // Trigger all added listeners and return whether any listener returned true
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return onNavigationItemSelectedListeners
            .map { it.onNavigationItemSelected(item) }
            .fold(false) { acc, result -> acc || result }
    }
}
