package com.example.wheel

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.demo_full.R

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val floatingLabel: TextView
    private val editText: EditText

    init {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this, true)

        floatingLabel = findViewById(R.id.floatingLabel)
        editText = findViewById(R.id.editText)

        // Set up the EditText focus listener
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || editText.text.isNotEmpty()) {
                showFloatingLabel()
            } else {
                hideFloatingLabel()
            }
        }

        // Watch for text changes to handle floating label
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() && !editText.hasFocus()) {
                    hideFloatingLabel()
                } else {
                    showFloatingLabel()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showFloatingLabel() {
        if (!floatingLabel.isVisible) {
            floatingLabel.isVisible = true
            floatingLabel.animate()
                .translationY(-30f)  // Move up
                .alpha(1f)  // Make visible
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(200)
                .start()
        }
    }

    private fun hideFloatingLabel() {
        if (floatingLabel.isVisible && editText.text.isEmpty()) {
            floatingLabel.animate()
                .translationY(0f)  // Move back to original position
                .alpha(0f)  // Hide
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(200)
                .withEndAction { floatingLabel.isVisible = false }
                .start()
        }
    }

    // Optional: Allow users to interact with EditText directly
    fun getEditText(): EditText = editText
}