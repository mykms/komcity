package ru.komcity.mobile.viewModel.ImageLoader

import android.graphics.*
import com.squareup.picasso.Transformation

class ImageTransform(private val key: String, private val isCircle: Boolean) : Transformation {

    companion object {
        val CIRCLE = ImageTransform("circle", true)
        val ROUNDED = ImageTransform("rounded", false)
    }

    override fun transform(source: Bitmap?): Bitmap {
        source?.let {
            val size = Math.min(it.width, it.height)

            val x = (it.width - size) / 2
            val y = (it.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(it, x, y, size, size)
            if (squaredBitmap != it) {
                it.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, it.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true

            if (isCircle) {
                val r = size / 2f
                canvas.drawCircle(r, r, r, paint)
            } else {
                val r = size / 7f
                val margin = 0f
                canvas.drawRoundRect(RectF(margin, margin, it.width * 1f, it.height * 1f), r, r, paint)
            }

            squaredBitmap.recycle()
            return bitmap
        }
        return Bitmap.createBitmap(50, 50, Bitmap.Config.ALPHA_8)
    }

    override fun key(): String {
        return key
    }
}