package com.example.blur

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class BlurTransformation(context: Context?, radius: Float) : BitmapTransformation() {
    private val rs: RenderScript
    private val blurRadius: Float

    init {
        rs = RenderScript.create(context)
        blurRadius = radius
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int,
    ): Bitmap {
        return blurBitmap(toTransform)
    }

    private fun blurBitmap(bitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap)
        val input: Allocation = Allocation.createFromBitmap(rs, bitmap)
        val output: Allocation = Allocation.createFromBitmap(rs, outputBitmap)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(outputBitmap)
        return outputBitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun equals(o: Any?): Boolean {
        return o is BlurTransformation && o.blurRadius == blurRadius
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "com.example.BlurTransformation." + VERSION
        private val ID_BYTES = ID.toByteArray(CHARSET)
    }
}