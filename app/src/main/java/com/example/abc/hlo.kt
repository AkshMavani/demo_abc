package com.example.abc

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import com.example.demo_full.R
import com.google.android.material.internal.ManufacturerUtils
import com.google.android.material.internal.ThemeEnforcement
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.theme.overlay.MaterialThemeOverlay

@SuppressLint("RestrictedApi")
class hlo @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle, ) :
    AppCompatEditText(
        MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, 0),
        attrs,
        defStyleAttr
    ) {
    private val parentRect = Rect()
    /**
     * Whether the edit text is using the TextInputLayout's focused rectangle.
     */
    /**
     * Whether the edit text should use the TextInputLayout's focused rectangle.
     */
    var isTextInputLayoutFocusedRectEnabled = false

    init {
        val attributes = ThemeEnforcement.obtainStyledAttributes(
            context,
            attrs,
            R.styleable.TextInputEditText,
            defStyleAttr,
            R.style.Widget_Design_TextInputEditText
        )
        isTextInputLayoutFocusedRectEnabled = attributes.getBoolean(
            R.styleable.TextInputEditText_textInputLayoutFocusedRectEnabled,
            false
        )
        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // Meizu devices expect TextView#mHintLayout to be non-null if TextView#getHint() is non-null.
        // In order to avoid crashing, we force the creation of the layout by setting an empty non-null
        // hint.
        val layout = textInputLayout
        if (layout != null && layout.isProvidingHint && super.getHint() == null && ManufacturerUtils.isMeizuDevice()) {
            hint = ""
        }
    }

    override fun getHint(): CharSequence? {
        // Certain test frameworks expect the actionable element to expose its hint as a label. When
        // TextInputLayout is providing our hint, retrieve it from the parent layout.
        val layout = textInputLayout
        return if (layout != null && layout.isProvidingHint) {
            layout.hint
        } else super.getHint()
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val ic = super.onCreateInputConnection(outAttrs)
        if (ic != null && outAttrs.hintText == null) {
            // If we don't have a hint and our parent is a TextInputLayout, use its hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            outAttrs.hintText = hintFromLayout
        }
        return ic
    }

    private val textInputLayout: TextInputLayout?
        private get() {
            var parent = parent
            while (parent is View) {
                if (parent is TextInputLayout) {
                    return parent
                }
                parent = parent.getParent()
            }
            return null
        }
    private val hintFromLayout: CharSequence?
        private get() {
            val layout = textInputLayout
            return layout?.hint
        }

    override fun getFocusedRect(r: Rect?) {
        super.getFocusedRect(r)
        val textInputLayout = textInputLayout
        if (textInputLayout != null && isTextInputLayoutFocusedRectEnabled && r != null) {
            textInputLayout.getFocusedRect(parentRect)
            r.bottom = parentRect.bottom
        }
    }

    override fun getGlobalVisibleRect(r: Rect?, globalOffset: Point?): Boolean {
        val result = super.getGlobalVisibleRect(r, globalOffset)
        val textInputLayout = textInputLayout
        if (textInputLayout != null && isTextInputLayoutFocusedRectEnabled && r != null) {
            textInputLayout.getGlobalVisibleRect(parentRect, globalOffset)
            r.bottom = parentRect.bottom
        }
        return result
    }

    override fun requestRectangleOnScreen(rectangle: Rect?): Boolean {
        val result = super.requestRectangleOnScreen(rectangle)
        val textInputLayout = textInputLayout
        if (textInputLayout != null && isTextInputLayoutFocusedRectEnabled) {
            parentRect[0, textInputLayout.height
                    - resources.getDimensionPixelOffset(R.dimen.mtrl_edittext_rectangle_top_offset), textInputLayout.width] =
                textInputLayout.height
            textInputLayout.requestRectangleOnScreen(parentRect, true)
        }
        return result
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        val layout = textInputLayout

        // In APIs < 23, some things set in the parent TextInputLayout's AccessibilityDelegate get
        // overwritten, so we set them here so that announcements are as expected.
        if (Build.VERSION.SDK_INT < 23 && layout != null) {
            info.text = getAccessibilityNodeInfoText(layout)
        }
    }

    private fun getAccessibilityNodeInfoText(layout: TextInputLayout): String {
        val inputText: CharSequence? = text
        val hintText = layout.hint
        val showingText = !TextUtils.isEmpty(inputText)
        val hasHint = !TextUtils.isEmpty(hintText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            labelFor = R.id.textinput_helper_text
        }
        val hint = if (hasHint) hintText.toString() else ""
        return if (showingText) {
            inputText.toString() + if (!TextUtils.isEmpty(hint)) ", $hint" else ""
        } else if (!TextUtils.isEmpty(hint)) {
            hint
        } else {
            ""
        }
    }
}
